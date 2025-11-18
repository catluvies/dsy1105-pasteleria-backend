package pasteleria.com.pasteleria.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

/**
 * Esta clase se ejecuta MUY TEMPRANO en el ciclo de vida de Spring,
 * ANTES de que se inicialice el DataSource.
 *
 * IMPORTANTE: Esta clase debe estar registrada en:
 * src/main/resources/META-INF/spring.factories
 */
public class OracleWalletConfig implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        System.out.println("=================================================================");
        System.out.println("OracleWalletConfig: Iniciando configuración de wallet...");
        System.out.println("=================================================================");

        try {
            String cwalletSsoBase64 = environment.getProperty("oracle.wallet.cwallet.sso");
            String ewalletP12Base64 = environment.getProperty("oracle.wallet.ewallet.p12");
            String ewalletPemBase64 = environment.getProperty("oracle.wallet.ewallet.pem");
            String keystoreJksBase64 = environment.getProperty("oracle.wallet.keystore.jks");
            String ojdbcPropertiesBase64 = environment.getProperty("oracle.wallet.ojdbc.properties");
            String sqlnetOraBase64 = environment.getProperty("oracle.wallet.sqlnet.ora");
            String tnsnamesOraBase64 = environment.getProperty("oracle.wallet.tnsnames.ora");
            String truststoreJksBase64 = environment.getProperty("oracle.wallet.truststore.jks");
            String walletDir = environment.getProperty("oracle.wallet.dir");

            // Si no hay variables de entorno configuradas, usar wallet local (desarrollo)
            if (cwalletSsoBase64 == null && walletDir == null) {
                System.out.println("⚠️  No wallet configuration found in environment variables.");
                System.out.println("⚠️  Using local wallet if configured in datasource URL.");
                System.out.println("=================================================================");
                return;
            }

            // Si walletDir está configurado, usar ese directorio (desarrollo local)
            if (walletDir != null && !walletDir.isEmpty()) {
                System.setProperty("oracle.net.tns_admin", walletDir);
                System.out.println("✅ Using wallet directory: " + walletDir);
                System.out.println("=================================================================");
                return;
            }

            // Verificar que al menos tengamos los archivos mínimos
            if (cwalletSsoBase64 == null || tnsnamesOraBase64 == null) {
                System.out.println("❌ ERROR: Missing required wallet files!");
                System.out.println("   cwallet.sso present: " + (cwalletSsoBase64 != null));
                System.out.println("   tnsnames.ora present: " + (tnsnamesOraBase64 != null));
                System.out.println("=================================================================");
                return;
            }

            // Crear directorio temporal para la wallet
            Path tempWalletDir = Files.createTempDirectory("oracle_wallet");
            System.out.println("📁 Creating temporary wallet directory: " + tempWalletDir);

            // Decodificar y escribir archivos de wallet
            int filesWritten = 0;
            if (cwalletSsoBase64 != null && !cwalletSsoBase64.isEmpty()) {
                writeBase64File(tempWalletDir, "cwallet.sso", cwalletSsoBase64);
                filesWritten++;
            }
            if (ewalletP12Base64 != null && !ewalletP12Base64.isEmpty()) {
                writeBase64File(tempWalletDir, "ewallet.p12", ewalletP12Base64);
                filesWritten++;
            }
            if (ewalletPemBase64 != null && !ewalletPemBase64.isEmpty()) {
                writeBase64File(tempWalletDir, "ewallet.pem", ewalletPemBase64);
                filesWritten++;
            }
            if (keystoreJksBase64 != null && !keystoreJksBase64.isEmpty()) {
                writeBase64File(tempWalletDir, "keystore.jks", keystoreJksBase64);
                filesWritten++;
            }
            if (ojdbcPropertiesBase64 != null && !ojdbcPropertiesBase64.isEmpty()) {
                writeBase64File(tempWalletDir, "ojdbc.properties", ojdbcPropertiesBase64);
                filesWritten++;
            }
            if (sqlnetOraBase64 != null && !sqlnetOraBase64.isEmpty()) {
                writeBase64File(tempWalletDir, "sqlnet.ora", sqlnetOraBase64);
                filesWritten++;
            }
            if (tnsnamesOraBase64 != null && !tnsnamesOraBase64.isEmpty()) {
                writeBase64File(tempWalletDir, "tnsnames.ora", tnsnamesOraBase64);
                filesWritten++;
            }
            if (truststoreJksBase64 != null && !truststoreJksBase64.isEmpty()) {
                writeBase64File(tempWalletDir, "truststore.jks", truststoreJksBase64);
                filesWritten++;
            }

            // Configurar propiedad del sistema para Oracle
            System.setProperty("oracle.net.tns_admin", tempWalletDir.toString());
            System.out.println("✅ Oracle wallet configured successfully!");
            System.out.println("   Location: " + tempWalletDir);
            System.out.println("   Files written: " + filesWritten);
            System.out.println("   TNS_ADMIN set to: " + tempWalletDir);
            System.out.println("=================================================================");

        } catch (Exception e) {
            System.err.println("❌ ERROR configuring Oracle wallet: " + e.getMessage());
            e.printStackTrace();
            System.out.println("=================================================================");
        }
    }

    private void writeBase64File(Path directory, String fileName, String base64Content) throws IOException {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64Content);
            Path filePath = directory.resolve(fileName);
            Files.write(filePath, decodedBytes);
            System.out.println("   ✓ Written: " + fileName + " (" + decodedBytes.length + " bytes)");
        } catch (Exception e) {
            System.err.println("   ✗ Failed to write " + fileName + ": " + e.getMessage());
            throw e;
        }
    }
}

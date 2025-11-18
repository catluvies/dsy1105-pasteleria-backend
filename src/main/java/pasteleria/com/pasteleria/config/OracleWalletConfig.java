package pasteleria.com.pasteleria.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

@Configuration
public class OracleWalletConfig {

    @Value("${oracle.wallet.cwallet.sso:#{null}}")
    private String cwalletSsoBase64;

    @Value("${oracle.wallet.ewallet.p12:#{null}}")
    private String ewalletP12Base64;

    @Value("${oracle.wallet.ewallet.pem:#{null}}")
    private String ewalletPemBase64;

    @Value("${oracle.wallet.keystore.jks:#{null}}")
    private String keystoreJksBase64;

    @Value("${oracle.wallet.ojdbc.properties:#{null}}")
    private String ojdbcPropertiesBase64;

    @Value("${oracle.wallet.sqlnet.ora:#{null}}")
    private String sqlnetOraBase64;

    @Value("${oracle.wallet.tnsnames.ora:#{null}}")
    private String tnsnamesOraBase64;

    @Value("${oracle.wallet.truststore.jks:#{null}}")
    private String truststoreJksBase64;

    @Value("${oracle.wallet.dir:#{null}}")
    private String walletDir;

    @PostConstruct
    public void setupWallet() throws IOException {
        // Si no hay variables de entorno configuradas, usar wallet local (desarrollo)
        if (cwalletSsoBase64 == null && walletDir == null) {
            System.out.println("No wallet configuration found in environment variables. Using local wallet if configured.");
            return;
        }

        // Si walletDir está configurado, usar ese directorio (desarrollo local)
        if (walletDir != null) {
            System.setProperty("oracle.net.tns_admin", walletDir);
            System.out.println("Using wallet directory: " + walletDir);
            return;
        }

        // Crear directorio temporal para la wallet
        Path tempWalletDir = Files.createTempDirectory("oracle_wallet");
        System.out.println("Creating temporary wallet directory: " + tempWalletDir);

        // Decodificar y escribir archivos de wallet
        if (cwalletSsoBase64 != null) {
            writeBase64File(tempWalletDir, "cwallet.sso", cwalletSsoBase64);
        }
        if (ewalletP12Base64 != null) {
            writeBase64File(tempWalletDir, "ewallet.p12", ewalletP12Base64);
        }
        if (ewalletPemBase64 != null) {
            writeBase64File(tempWalletDir, "ewallet.pem", ewalletPemBase64);
        }
        if (keystoreJksBase64 != null) {
            writeBase64File(tempWalletDir, "keystore.jks", keystoreJksBase64);
        }
        if (ojdbcPropertiesBase64 != null) {
            writeBase64File(tempWalletDir, "ojdbc.properties", ojdbcPropertiesBase64);
        }
        if (sqlnetOraBase64 != null) {
            writeBase64File(tempWalletDir, "sqlnet.ora", sqlnetOraBase64);
        }
        if (tnsnamesOraBase64 != null) {
            writeBase64File(tempWalletDir, "tnsnames.ora", tnsnamesOraBase64);
        }
        if (truststoreJksBase64 != null) {
            writeBase64File(tempWalletDir, "truststore.jks", truststoreJksBase64);
        }

        // Configurar propiedad del sistema para Oracle
        System.setProperty("oracle.net.tns_admin", tempWalletDir.toString());
        System.out.println("Oracle wallet configured successfully at: " + tempWalletDir);
    }

    private void writeBase64File(Path directory, String fileName, String base64Content) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(base64Content);
        Path filePath = directory.resolve(fileName);
        Files.write(filePath, decodedBytes);
        System.out.println("Written wallet file: " + fileName);
    }
}

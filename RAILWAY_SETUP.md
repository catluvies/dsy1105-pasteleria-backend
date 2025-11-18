# Configuración de Railway para Pastelería Backend

Este documento explica cómo configurar las variables de entorno necesarias para desplegar la aplicación en Railway con Oracle Autonomous Database.

## ⚠️ IMPORTANTE: Seguridad

**NUNCA** subas archivos de wallet o credenciales a GitHub. Este proyecto usa codificación Base64 de archivos de wallet como variables de entorno en Railway.

## 📋 Pasos para configurar Railway

### 1. Generar variables de entorno Base64

Ejecuta el siguiente script en tu terminal local (Git Bash en Windows):

```bash
bash encode-wallet.sh
```

Este script generará todas las variables de entorno necesarias codificadas en Base64.

### 2. Configurar variables en Railway

Ve a tu proyecto en Railway → **Variables** y agrega las siguientes variables:

#### Variables de la Wallet (copiar del output del script)

```
ORACLE_WALLET_CWALLET_SSO=<valor-generado-por-script>
ORACLE_WALLET_EWALLET_P12=<valor-generado-por-script>
ORACLE_WALLET_EWALLET_PEM=<valor-generado-por-script>
ORACLE_WALLET_KEYSTORE_JKS=<valor-generado-por-script>
ORACLE_WALLET_OJDBC_PROPERTIES=<valor-generado-por-script>
ORACLE_WALLET_SQLNET_ORA=<valor-generado-por-script>
ORACLE_WALLET_TNSNAMES_ORA=<valor-generado-por-script>
ORACLE_WALLET_TRUSTSTORE_JKS=<valor-generado-por-script>
```

#### Variables de conexión a la base de datos

```
SPRING_DATASOURCE_URL=jdbc:oracle:thin:@pasteleriamoviles_high?TNS_ADMIN=/tmp/oracle_wallet
SPRING_DATASOURCE_USERNAME=ADMIN
SPRING_DATASOURCE_PASSWORD=Pasteleria#25
```

### 3. Verificar el despliegue

Después de agregar las variables:

1. Railway automáticamente re-desplegará tu aplicación
2. Revisa los logs en Railway → **Deploy Logs**
3. Busca el mensaje: `Oracle wallet configured successfully at: /tmp/oracle_wallet...`
4. Si ves errores de conexión, verifica que todas las variables estén configuradas correctamente

## 🔧 Cómo funciona

1. **Desarrollo Local**: La aplicación usa la wallet local en `C:/Users/anyar/projects/Wallet_PasteleriaMoviles`

2. **Railway (Producción)**:
   - Al iniciar, la clase `OracleWalletConfig` detecta las variables de entorno
   - Decodifica los archivos Base64
   - Crea un directorio temporal `/tmp/oracle_wallet`
   - Escribe los archivos de wallet decodificados
   - Configura la propiedad `oracle.net.tns_admin` para que Oracle JDBC los use

## 📝 Notas adicionales

- Los archivos de wallet se recrean en cada despliegue (son temporales)
- Las variables de entorno están encriptadas en Railway
- Asegúrate de que el `.gitignore` esté configurado correctamente para excluir wallets locales

## 🐛 Troubleshooting

### Error: "ORA-12263: Failed to access tnsnames.ora"

**Causa**: Las variables de entorno de wallet no están configuradas en Railway

**Solución**: Verifica que todas las variables `ORACLE_WALLET_*` estén presentes en Railway

### Error: "Unable to open JDBC Connection"

**Causa**: Credenciales incorrectas o URL de conexión mal formada

**Solución**: Verifica las variables:
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

### La aplicación no se conecta después de configurar variables

**Solución**:
1. Revisa los logs de Railway
2. Busca el mensaje de confirmación de wallet
3. Verifica que no haya caracteres especiales mal escapados en las variables

## 🔐 Buenas prácticas de seguridad

✅ **SÍ hacer:**
- Usar variables de entorno para credenciales
- Mantener archivos de wallet fuera de Git
- Rotar contraseñas periódicamente
- Limitar acceso a las variables de Railway

❌ **NO hacer:**
- Subir wallets a GitHub
- Compartir credenciales en código
- Hardcodear passwords
- Compartir públicamente el output del script `encode-wallet.sh`

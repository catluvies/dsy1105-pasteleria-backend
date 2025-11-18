#!/bin/bash

# Script para codificar archivos de wallet de Oracle a Base64
# Uso: ./encode-wallet.sh <ruta-a-wallet>

WALLET_DIR="${1:-C:/Users/anyar/projects/Wallet_PasteleriaMoviles}"

if [ ! -d "$WALLET_DIR" ]; then
    echo "Error: Directorio de wallet no encontrado: $WALLET_DIR"
    exit 1
fi

echo "========================================"
echo "VARIABLES DE ENTORNO PARA RAILWAY"
echo "========================================"
echo ""
echo "Copia estas variables en Railway Dashboard > Variables:"
echo ""

# Función para codificar archivo
encode_file() {
    local file="$1"
    local var_name="$2"

    if [ -f "$WALLET_DIR/$file" ]; then
        echo "# $file"
        echo -n "$var_name="
        base64 -w 0 "$WALLET_DIR/$file" 2>/dev/null || base64 "$WALLET_DIR/$file" | tr -d '\n'
        echo ""
        echo ""
    else
        echo "# $file no encontrado - OMITIDO"
        echo ""
    fi
}

# Codificar cada archivo
encode_file "cwallet.sso" "ORACLE_WALLET_CWALLET_SSO"
encode_file "ewallet.p12" "ORACLE_WALLET_EWALLET_P12"
encode_file "ewallet.pem" "ORACLE_WALLET_EWALLET_PEM"
encode_file "keystore.jks" "ORACLE_WALLET_KEYSTORE_JKS"
encode_file "ojdbc.properties" "ORACLE_WALLET_OJDBC_PROPERTIES"
encode_file "sqlnet.ora" "ORACLE_WALLET_SQLNET_ORA"
encode_file "tnsnames.ora" "ORACLE_WALLET_TNSNAMES_ORA"
encode_file "truststore.jks" "ORACLE_WALLET_TRUSTSTORE_JKS"

echo "========================================"
echo "OTRAS VARIABLES NECESARIAS:"
echo "========================================"
echo ""
echo "SPRING_DATASOURCE_URL=jdbc:oracle:thin:@pasteleriamoviles_high?TNS_ADMIN=/tmp/oracle_wallet"
echo "SPRING_DATASOURCE_USERNAME=_"
echo "SPRING_DATASOURCE_PASSWORD=_"
echo ""
echo "========================================"
echo "NOTA: Guarda estas variables de forma segura."
echo "No las subas a GitHub ni las compartas públicamente."
echo "========================================"

@echo off
setlocal EnableDelayedExpansion

jarsigner -keystore NONE -storetype PKCS11 -storepass %2 -tsa http://rfc3161timestamp.globalsign.com/advanced -providerClass sun.security.pkcs11.SunPKCS11 -providerArg %NIAGARA_HOME%\..\.btib\certificates\eToken.cfg %NIAGARA_HOME%\modules\%1.jar %3

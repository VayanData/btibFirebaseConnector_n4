@echo off
setlocal EnableDelayedExpansion

GOTO:REM
---------------------------------------------------------------------------------------------------
Run all the procedure to install dependencies, build the module, and obfuscate it.
It relies on the NIAGARA_HOME environment property.
---------------------------------------------------------------------------------------------------
:REM

:: %1 -> storepass
:: %2 -> signature alias
IF [%2]==[] (
    echo Error: storepass and signature alias not defined
    echo usage: ^<storepass^> ^<signature_alias^>
    exit /b
)

call :removeJars 2>&1

cd "%~dp0\.."

call gradlew clean
IF %ERRORLEVEL% NEQ 0 ( 
   exit %ERRORLEVEL% 
)

call gradlew build
IF %ERRORLEVEL% NEQ 0 ( 
   exit %ERRORLEVEL% 
)

call gradlew --stop
IF %ERRORLEVEL% NEQ 0 ( 
   exit %ERRORLEVEL% 
)

:: Obfuscation
call scripts\obfuscation.bat btibFirebaseConnector
IF %ERRORLEVEL% NEQ 0 ( 
   exit %ERRORLEVEL% 
)

:: Signature
echo Remove Niagara auto-generated signature...
call %NIAGARA_HOME%\..\.btib\lib\7-Zip\7z d %NIAGARA_HOME%\modules\btibFirebaseConnector-rt.jar META-INF\*.RSA META-INF\*.SF -bso0
call %NIAGARA_HOME%\..\.btib\lib\7-Zip\7z d %NIAGARA_HOME%\modules\btibFirebaseConnector-wb.jar META-INF\*.RSA META-INF\*.SF -bso0

echo Signing with USB Token...
call scripts\signer.bat btibFirebaseConnector-rt %1 %2
call scripts\signer.bat btibFirebaseConnector-wb %1 %2

exit %ERRORLEVEL%

:removeJars
IF EXIST %NIAGARA_HOME%\modules\btibFirebaseConnector-* (
    del %NIAGARA_HOME%\modules\btibFirebaseConnector-*
)
exit /b
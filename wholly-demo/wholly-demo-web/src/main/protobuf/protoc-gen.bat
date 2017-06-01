cd /d %~dp0

if "%1" == "" goto SET_DEFAULT_GEN_PATH
set GEN_PATH=%1
goto SET_FILENAME

:SET_DEFAULT_GEN_PATH
set GEN_PATH=.

:SET_FILENAME
if "%2" == "" goto SET_DEFAULT_FILENAME
set GEN_FILENAME=%2
goto SET_OUT_PATH

:SET_DEFAULT_FILENAME
set GEN_FILENAME=EnumType.proto

:SET_OUT_PATH
if "%3" == "" goto SET_DEFAULT_OUT_PATH
set OUT_PATH=%3
goto RUN_GEN

:SET_DEFAULT_OUT_PATH
set OUT_PATH=../java/

:RUN_GEN
protoc --proto_path=%GEN_PATH% --java_out=%OUT_PATH% %GEN_FILENAME%

rem exit


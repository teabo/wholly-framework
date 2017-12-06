cd /d %~dp0

if "%1" == "" goto SET_DEFAULT_GEN_PATH
set GEN_PATH=%1
goto SET_OUT_PATH

:SET_DEFAULT_GEN_PATH
set GEN_PATH=./demoHello.thrift

:SET_OUT_PATH
if "%2" == "" goto SET_DEFAULT_OUT_PATH
set OUT_PATH=%2
goto RUN_GEN

:SET_DEFAULT_OUT_PATH
set OUT_PATH=../java/

:RUN_GEN
thrift-0.9.2.exe -out %OUT_PATH% -r -gen java %GEN_PATH%

exit
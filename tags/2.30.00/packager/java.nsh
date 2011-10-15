!define JRE_MAJOR "1"
!define JRE_MINOR "6"
!define JRE_URL "http://javadl.sun.com/webapps/download/AutoDL?BundleId=49024"

Function GetJRE
    IfFileExists "jre-6u26-windows-i586-s.exe" lbl_NoDownloadRequired lbl_DownloadRequired
	
    lbl_NoDownloadRequired:
      ExecWait jre-6u26-windows-i586-s.exe
      GoTo done
	
    lbl_DownloadRequired:
	MessageBox MB_OK "$(^Name) uses Java ${JRE_MAJOR}.${JRE_MINOR}, it will now \
                         be downloaded and installed"
 
	StrCpy $2 "$TEMP\jre-6u26-windows-i586-s.exe"
	nsisdl::download /TIMEOUT=30000 ${JRE_URL} $2
	Pop $R0 ;Get the return value
	StrCmp $R0 "success" +3
	MessageBox MB_OK "Download failed: $R0"
      Quit
	ExecWait $2
	Delete $2
    done:
FunctionEnd
 
 
Function DetectJRE
	ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
	IfErrors checkWoW64
	GoTo checkVersion
  
  checkWoW64:
	SetRegView 32
	ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
	SetRegView lastused
	IfErrors goNext
	
  checkVersion:
	StrCpy $4 $2 1 0
	IntCmp $4 ${JRE_MAJOR} +1 goNext done
	StrCpy $4 $2 1 2
	IntCmp $4 ${JRE_MINOR} done goNext done

  goNext:
	Call GetJRE
  
  done:
FunctionEnd

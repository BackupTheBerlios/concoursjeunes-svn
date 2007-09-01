# Auto-generated by EclipseNSIS Script Wizard
# 4 ao�t 2006 11:30:01

Name ConcoursJeunes
SetCompressor lzma

# Defines
!define REGKEY "SOFTWARE\concoursjeunes.org\$(^Name)"
!ifndef VERSION
	!define VERSION 2.0
!endif
!define COMPANY ConcoursJeunes.org
!define URL http://www.concoursjeunes.org
!ifndef OUT_FILE
	!define OUT_FILE setup.exe
!endif

!ifdef WORKING_DIR
	!cd ${WORKING_DIR}
!endif

# MUI defines
!define MUI_ICON "${NSISDIR}\Contrib\Graphics\Icons\modern-install.ico"
!define MUI_FINISHPAGE_NOAUTOCLOSE
!define MUI_STARTMENUPAGE_REGISTRY_ROOT HKLM
!define MUI_STARTMENUPAGE_NODISABLE
!define MUI_STARTMENUPAGE_REGISTRY_KEY Software\concoursjeunes.org\ConcoursJeunes
!define MUI_STARTMENUPAGE_REGISTRY_VALUENAME StartMenuGroup
!define MUI_STARTMENUPAGE_DEFAULT_FOLDER ConcoursJeunes
!define MUI_UNICON "${NSISDIR}\Contrib\Graphics\Icons\modern-uninstall.ico"
!define MUI_LANGDLL_REGISTRY_ROOT HKLM
!define MUI_LANGDLL_REGISTRY_KEY ${REGKEY}
!define MUI_LANGDLL_REGISTRY_VALUENAME InstallerLanguage

# Included files
!include Sections.nsh
!include MUI.nsh

# Reserved Files
!insertmacro MUI_RESERVEFILE_LANGDLL

# Variables
Var StartMenuGroup

# Installer pages
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE Licence_win.txt
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_COMPONENTS
!insertmacro MUI_PAGE_STARTMENU Application $StartMenuGroup
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES

# Installer languages
!insertmacro MUI_LANGUAGE French
!insertmacro MUI_LANGUAGE English

# Installer attributes
OutFile ${OUT_FILE}
InstallDir $PROGRAMFILES\ConcoursJeunes
CRCCheck on
XPStyle on
ShowInstDetails show
VIProductVersion 1.0.0.0
VIAddVersionKey /LANG=${LANG_FRENCH} ProductName "ConcoursJeunes Setup"
VIAddVersionKey ProductVersion "${VERSION}"
VIAddVersionKey /LANG=${LANG_FRENCH} CompanyName "${COMPANY}"
VIAddVersionKey /LANG=${LANG_FRENCH} CompanyWebsite "${URL}"
VIAddVersionKey /LANG=${LANG_FRENCH} FileVersion ""
VIAddVersionKey /LANG=${LANG_FRENCH} FileDescription ""
VIAddVersionKey /LANG=${LANG_FRENCH} LegalCopyright "(c) 2007 Concoursjeunes.org"
InstallDirRegKey HKLM "${REGKEY}" Path
ShowUninstDetails show

InstType "Complete"
#InstType "normal"

# Installer sections
Section "Base" SEC0000
    SectionIn RO 1
    SetOverwrite on
    SetOutPath $INSTDIR\config
    File /r config\*
    SetOutPath $INSTDIR\lang
    File /r lang\*
    SetOutPath $INSTDIR\lib
    File /r lib\*
    SetOutPath $INSTDIR\ressources
    File /r ressources\*
    SetOutPath $INSTDIR\documentation
    File /r documentation\*
    SetOutPath $INSTDIR
    File ConcoursJeunes.jar
    File /r plugins\ConcoursJeunesUpdate\*
    File *.txt
    File hash.xml.gz
    File windows\concoursjeunes-applyupdate.exe
    File windows\concoursjeunes-applyupdate.exe.manifest
    WriteRegStr HKLM "${REGKEY}\Components" Base 1
SectionEnd

Section "Import Result'Arc" SEC0001
    SectionIn 1
    SetOverwrite on
    SetOutPath $INSTDIR
    File /r plugins\FFTAImport\*
    File hash.xml.gz
    WriteRegStr HKLM "${REGKEY}\Components" "Import Result'Arc" 1
SectionEnd

Section -post SEC0002
    WriteRegStr HKLM "${REGKEY}" Path $INSTDIR
    WriteUninstaller $INSTDIR\uninstall.exe
    !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
    SetOutPath $SMPROGRAMS\$StartMenuGroup
    CreateShortcut "$SMPROGRAMS\$StartMenuGroup\$(^UninstallLink).lnk" $INSTDIR\uninstall.exe
    SetOutPath $INSTDIR
    CreateShortCut "$SMPROGRAMS\$StartMenuGroup\ConcoursJeunes.lnk" "$SYSDIR\javaw.exe" '-Xmx128m -jar "$INSTDIR\ConcoursJeunes.jar"' "$INSTDIR\ressources\iconCJ.ico"
    !insertmacro MUI_STARTMENU_WRITE_END
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayName "$(^Name)"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayVersion "${VERSION}"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" Publisher "${COMPANY}"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" URLInfoAbout "${URL}"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayIcon $INSTDIR\uninstall.exe
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" UninstallString $INSTDIR\uninstall.exe
    WriteRegDWORD HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" NoModify 1
    WriteRegDWORD HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" NoRepair 1
SectionEnd

# Macro for selecting uninstaller sections
!macro SELECT_UNSECTION SECTION_NAME UNSECTION_ID
    Push $R0
    ReadRegStr $R0 HKLM "${REGKEY}\Components" "${SECTION_NAME}"
    StrCmp $R0 1 0 next${UNSECTION_ID}
    !insertmacro SelectSection "${UNSECTION_ID}"
    GoTo done${UNSECTION_ID}
next${UNSECTION_ID}:
    !insertmacro UnselectSection "${UNSECTION_ID}"
done${UNSECTION_ID}:
    Pop $R0
!macroend

# Uninstaller sections
Section /o un.Base UNSEC0000
    RmDir /r /REBOOTOK $INSTDIR
    DeleteRegValue HKLM "${REGKEY}\Components" Base
SectionEnd

Section /o "un.Import Reslut'Arc" UNSEC0001
    RmDir /r /REBOOTOK $INSTDIR\plugins\FFTAImport
    DeleteRegValue HKLM "${REGKEY}\Components" "Import Result'Arc"
SectionEnd

Section un.post UNSEC0002
    DeleteRegKey HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)"
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\$(^UninstallLink).lnk"
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\ConcoursJeunes.lnk"
    Delete /REBOOTOK $INSTDIR\uninstall.exe
    DeleteRegValue HKLM "${REGKEY}" StartMenuGroup
    DeleteRegValue HKLM "${REGKEY}" Path
    DeleteRegKey /IfEmpty HKLM "${REGKEY}\Components"
    DeleteRegKey /IfEmpty HKLM "${REGKEY}"
    RmDir /REBOOTOK $SMPROGRAMS\$StartMenuGroup
    RmDir /REBOOTOK $INSTDIR
SectionEnd

# Installer functions
Function .onInit
    InitPluginsDir
    !insertmacro MUI_LANGDLL_DISPLAY
FunctionEnd

# Uninstaller functions
Function un.onInit
    SetAutoClose true
    ReadRegStr $INSTDIR HKLM "${REGKEY}" Path
    ReadRegStr $StartMenuGroup HKLM "${REGKEY}" StartMenuGroup
    !insertmacro MUI_UNGETLANGUAGE
    !insertmacro SELECT_UNSECTION Base ${UNSEC0000}
FunctionEnd


# Installer Language Strings

LangString ^UninstallLink ${LANG_FRENCH} "Uninstall $(^Name)"
LangString ^UninstallLink ${LANG_ENGLISH} "Uninstall $(^Name)"

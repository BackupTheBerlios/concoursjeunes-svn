# Initial spec file created by autospec ver. 0.8 with rpm 3 compatibility
Summary: @en.summary@
# The Summary: line should be expanded to about here -----^
Summary(fr): @fr.summary@
Name: @version.name@
Version: @version.numero@
Release: @version.release@
Group: Applications/Productivity
#Group(fr): (translated group goes here)
License: CeCILL, GPL
Source: @version.name@-@version.numero@.tar.gz
#NoSource: 0
BuildRoot: %{_tmppath}/%{name}-root
# Following are optional fields
URL: @version.url@
BuildArch: noarch

Requires: usermode
#Obsoletes: 
#BuildRequires: 

%description
@en.description@

#%description -l fr
@fr.description@

%prep
%setup -c 'ArcCompetition-%{version}'

%pre
groupadd -f arccompetition

%install
[ "$RPM_BUILD_ROOT" != "/" ] && rm -rf "$RPM_BUILD_ROOT"

%__cp -a . "${RPM_BUILD_ROOT-/}"

ln -s consolehelper "$RPM_BUILD_ROOT%{_bindir}/arccompetition-applyupdate"
rm -rf "$RPM_BUILD_ROOT/Makefile"

%clean
[ "$RPM_BUILD_ROOT" != "/" ] && rm -rf "$RPM_BUILD_ROOT"

%files
%defattr(-,root,root)
/usr/share/ArcCompetition/*.txt
/usr/share/ArcCompetition/config/*
/usr/share/ArcCompetition/lang/*
/usr/share/ArcCompetition/lib/*
/usr/share/ArcCompetition/documentation/*
/usr/share/ArcCompetition/plugins/*
/usr/share/ArcCompetition/ressources/*
/usr/share/ArcCompetition/ArcCompetition.jar
%attr(755, -, root) %{_bindir}/ArcCompetition
%attr(755, -, root) %{_bindir}/arccompetition-applyupdate
%attr(700, root, root) /usr/sbin/arccompetition-applyupdate
%attr(644, root, root) %{_sysconfdir}/pam.d/arccompetition-applyupdate
%attr(644, root, root) %{_sysconfdir}/security/console.apps/arccompetition-applyupdate
%{_datadir}/pixmaps/ArcCompetition.xpm
%{_datadir}/applications/ArcCompetition.desktop
%attr(2777, root, arccompetition) /var/lib/ArcCompetition/

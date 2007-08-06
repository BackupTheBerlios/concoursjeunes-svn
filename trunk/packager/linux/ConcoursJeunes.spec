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
Source: ConcoursJeunes.tar.gz
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
%setup -c 'ConcoursJeunes-%{version}'

%install
[ "$RPM_BUILD_ROOT" != "/" ] && rm -rf $RPM_BUILD_ROOT

%__cp -a . "${RPM_BUILD_ROOT-/}"

ln -s consolehelper $RPM_BUILD_ROOT%{_bindir}/concoursjeunes-applyupdate
rm -rf $RPM_BUILD_ROOT/Makefile

%clean
[ "$RPM_BUILD_ROOT" != "/" ] && rm -rf "$RPM_BUILD_ROOT"

%files
%defattr(-,root,root)
/usr/lib/ConcoursJeunes/config/*
/usr/lib/ConcoursJeunes/lang/*
/usr/lib/ConcoursJeunes/lib/*
/usr/lib/ConcoursJeunes/plugins/*
/usr/lib/ConcoursJeunes/ressources/*
/usr/lib/ConcoursJeunes/ConcoursJeunes.jar
/usr/lib/ConcoursJeunes/hash.xml.gz
%attr(755, -, root) %{_bindir}/ConcoursJeunes
%attr(755, -, root) %{_bindir}/concoursjeunes-applyupdate
%attr(700, root, root) /usr/sbin/concoursjeunes-applyupdate
%attr(644, root, root) %{_sysconfdir}/pam.d/concoursjeunes-applyupdate
%attr(644, root, root) %{_sysconfdir}/security/console.apps/concoursjeunes-applyupdate
%{_datadir}/pixmaps/ConcoursJeunes.xpm
%{_datadir}/applications/ConcoursJeunes.desktop
%attr(777, root, root) /var/lib/ConcoursJeunes/*

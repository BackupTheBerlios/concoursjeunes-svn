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
#Requires: 
#Obsoletes: 
#BuildRequires: 

%description
@en.description@

#%description -l fr
@fr.description@

%prep
%setup -c 'ConcoursJeunes-%{version}'
#%patch

%install
%__cp -a . "${RPM_BUILD_ROOT-/}"

%clean
[ "$RPM_BUILD_ROOT" != "/" ] && rm -rf "$RPM_BUILD_ROOT"

%files
%defattr(-,root,root)
/usr/lib/ConcoursJeunes/base/*
/usr/lib/ConcoursJeunes/config/*
/usr/lib/ConcoursJeunes/lang/*
/usr/lib/ConcoursJeunes/lib/*
/usr/lib/ConcoursJeunes/plugins/*
/usr/lib/ConcoursJeunes/ressources/*
/usr/lib/ConcoursJeunes/ConcoursJeunes.jar
%attr(755, -, root) /usr/bin/ConcoursJeunes
/usr/share/pixmaps/ConcoursJeunes.xpm
/usr/share/applications/ConcoursJeunes.desktop

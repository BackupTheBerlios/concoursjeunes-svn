USE_CONSOLEHELPER=$(shell if [ -x /usr/bin/consolehelper ] ; then echo "yes" ; fi)
USE_GKSU=$(shell if [ -x /usr/bin/gksu ] ; then echo "yes" ; fi)
USE_KDESU=$(shell if [ -x /usr/bin/kdesu ] ; then echo "yes" ; fi)

all:

install:
	groupadd -f arccompetition
	
	mkdir -p "$(DESTDIR)/usr/share/ArcCompetition"
	mkdir -p "$(DESTDIR)/var/lib/ArcCompetition"
	mkdir -p "$(DESTDIR)/usr/bin/"
	mkdir -p "$(DESTDIR)/usr/sbin/"
	mkdir -p "$(DESTDIR)/usr/share/pixmaps"
	mkdir -p "$(DESTDIR)/usr/share/applications"
	
	cp -r usr/share/ArcCompetition/. "$(DESTDIR)/usr/share/ArcCompetition/"
	chmod -R a+Xr,u+w "$(DESTDIR)/usr/share/ArcCompetition"
	chown -R root:root "$(DESTDIR)/usr/share/ArcCompetition"
	install -m755 usr/bin/ArcCompetition "$(DESTDIR)/usr/bin/ArcCompetition"
	install -m644 usr/share/pixmaps/ArcCompetition.xpm "$(DESTDIR)/usr/share/pixmaps/ArcCompetition.xpm"
	install -m644 usr/share/applications/ArcCompetition.desktop "$(DESTDIR)/usr/share/applications/ArcCompetition.desktop"
	cp -r var/lib/ArcCompetition/. "$(DESTDIR)/var/lib/ArcCompetition/"
	chmod +s "$(DESTDIR)/var/lib/ArcCompetition"
	chmod -R a+Xrw "$(DESTDIR)/var/lib/ArcCompetition"
	chown -R root:arccompetition "$(DESTDIR)/var/lib/ArcCompetition"
	install -m750 usr/sbin/arccompetition-applyupdate "$(DESTDIR)/usr/sbin/arccompetition-applyupdate"

	
ifeq ($(USE_CONSOLEHELPER), yes)
	install -m644 etc/pam.d/arccompetition-applyupdate "$(DESTDIR)/etc/pam.d/arccompetition-applyupdate"
	install -m644 etc/security/console.apps/arccompetition-applyupdate "$(DESTDIR)/etc/security/console.apps/arccompetition-applyupdate"
	
	ln -s consolehelper "$(DESTDIR)/usr/bin/arccompetition-applyupdate"
else ifeq ($(USE_GKSU), yes)
	echo "#!/bin/sh" > "$(DESTDIR)/usr/bin/arccompetition-applyupdate"
	echo "gksu /usr/sbin/arccompetition-applyupdate \$$1 \$$2" >> "$(DESTDIR)/usr/bin/arccompetition-applyupdate"
	chmod 755 "$(DESTDIR)/usr/bin/arccompetition-applyupdate"
else ifeq ($(USE_KDESU), yes)
	echo "#!/bin/sh" > "$(DESTDIR)/usr/bin/arccompetition-applyupdate"
	echo "kdesu /usr/sbin/arccompetition-applyupdate \$$1 \$$2" >> "$(DESTDIR)/usr/bin/arccompetition-applyupdate"
	chmod 755 "$(DESTDIR)/usr/bin/arccompetition-applyupdate"
endif

clean:



remove:
	rm -rf "$(DESTDIR)/usr/share/ArcCompetition"
	rm -f "$(DESTDIR)/usr/bin/ArcCompetition"
	rm -f "$(DESTDIR)/usr/sbin/arccompetition-applyupdate"
	rm -f "$(DESTDIR)/usr/bin/arccompetition-applyupdate"
	rm -f "$(DESTDIR)/usr/share/pixmaps/ArcCompetition.xpm"
	rm -f "$(DESTDIR)/usr/share/applications/ArcCompetition.desktop"
ifeq ($(USE_CONSOLEHELPER), yes)
	rm -f "$(DESTDIR)/etc/pam.d/arccompetition-applyupdate"
	rm -f "$(DESTDIR)/etc/security/console.apps/arccompetition-applyupdate"
endif


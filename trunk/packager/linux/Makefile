USE_CONSOLEHELPER=$(shell if [ -x /usr/bin/consolehelper ] ; then echo "yes" ; fi)
USE_GKSU=$(shell if [ -x /usr/bin/gksu ] ; then echo "yes" ; fi)
USE_KDESU=$(shell if [ -x /usr/bin/kdesu ] ; then echo "yes" ; fi)

all:

install:

	mkdir -p $(DESTDIR)/usr/lib/ConcoursJeunes
	mkdir -p $(DESTDIR)/var/lib/ConcoursJeunes
	mkdir -p $(DESTDIR)/usr/bin/
	mkdir -p $(DESTDIR)/usr/sbin/
	mkdir -p $(DESTDIR)/usr/share/pixmaps
	mkdir -p $(DESTDIR)/usr/share/applications
	
	cp -r usr/lib/ConcoursJeunes/. $(DESTDIR)/usr/lib/ConcoursJeunes/
	chmod -R a+Xr,u+w $(DESTDIR)/usr/lib/ConcoursJeunes
	chown -R root:root $(DESTDIR)/usr/lib/ConcoursJeunes
	install -m755 usr/bin/ConcoursJeunes $(DESTDIR)/usr/bin/ConcoursJeunes
	install -m644 usr/share/pixmaps/ConcoursJeunes.xpm $(DESTDIR)/usr/share/pixmaps/ConcoursJeunes.xpm
	install -m644 usr/share/applications/ConcoursJeunes.desktop $(DESTDIR)/usr/share/applications/ConcoursJeunes.desktop
	cp -r var/lib/ConcoursJeunes/. $(DESTDIR)/var/lib/ConcoursJeunes/
	chmod -R a+Xrw $(DESTDIR)/var/lib/ConcoursJeunes
	chown -R root:root $(DESTDIR)/var/lib/ConcoursJeunes
	install -m700 usr/sbin/concoursjeunes-applyupdate $(DESTDIR)/usr/sbin/concoursjeunes-applyupdate

	
ifeq ($(USE_CONSOLEHELPER), yes)
	install -m644 etc/pam.d/concoursjeunes-applyupdate $(DESTDIR)/etc/pam.d/concoursjeunes-applyupdate
	install -m644 etc/security/console.apps/concoursjeunes-applyupdate $(DESTDIR)/etc/security/console.apps/concoursjeunes-applyupdate
	
	ln -s consolehelper $(DESTDIR)/usr/bin/concoursjeunes-applyupdate
else ifeq ($(USE_GKSU), yes)
	echo "gksu /usr/sbin/concoursjeunes-applyupdate \$1 \$2" > $(DESTDIR)/usr/bin/concoursjeunes-applyupdate
	chmod 755 $(DESTDIR)/usr/bin/concoursjeunes-applyupdate
else ifeq ($(USE_KDESU), yes)
	echo "kdesu /usr/sbin/concoursjeunes-applyupdate \$1 \$2" > $(DESTDIR)/usr/bin/concoursjeunes-applyupdate
	chmod 755 $(DESTDIR)/usr/bin/concoursjeunes-applyupdate
endif

clean:



remove:

	rm -rf $(DESTDIR)/usr/lib/ConcoursJeunes
	rm -f $(DESTDIR)/usr/bin/ConcoursJeunes
	rm -f $(DESTDIR)/usr/sbin/concoursjeunes-applyupdate
	rm -f $(DESTDIR)/usr/bin/concoursjeunes-applyupdate
	rm -f $(DESTDIR)/usr/share/pixmaps/ConcoursJeunes.xpm
	rm -f $(DESTDIR)/usr/share/applications/ConcoursJeunes.desktop
ifeq ($(USE_CONSOLEHELPER), yes)
	rm -f $(DESTDIR)/etc/pam.d/concoursjeunes-applyupdate
	rm -f $(DESTDIR)/etc/security/console.apps/concoursjeunes-applyupdate
endif


# See https://git.yoctoproject.org/poky/tree/meta/files/common-licenses
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# TODO: Set this  with the path to your assignments rep.  Use ssh protocol and see lecture notes
# about how to setup ssh-agent for passwordless access
SRC_URI = "git://git@github.com/cu-ecen-aeld/assignments-3-and-later-arvind747.git;protocol=ssh;branch=main"

PV = "1.0+git${SRCPV}"
# TODO: set to reference a specific commit hash in your assignment repo
SRCREV = "c49a272e9839b7ca225aa2fe58e117a1b03b3424"

# This sets your staging directory based on WORKDIR, where WORKDIR is defined at 
# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-WORKDIR
# We reference the "server" directory here to build from the "server" directory
# in your assignments repo
S = "${WORKDIR}/git/server"

# TODO: Add the aesdsocket application and any other files you need to install
# See https://git.yoctoproject.org/poky/plain/meta/conf/bitbake.conf?h=kirkstone
#FILES:${PN} += "${bindir}/aesdsocket"
# TODO: customize these as necessary for any libraries you need for your application
# (and remove comment)
TARGET_LDFLAGS += "-pthread -lrt"

# QA Issue: File /usr/bin/aesdsocket in package aesd-assignments doesn't have GNU_HASH
INSANE_SKIP:${PN} += "ldflags"

# TODO: Package the aesdsocket application and any other files you need to install on rootfs
# See https://git.yoctoproject.org/poky/plain/meta/conf/bitbake.conf?h=kirkstone
FILES:${PN} += "${bindir}/aesdsocket"
FILES:${PN} += "${sysconfdir}/aesdsocket-start-stop"
FILES:${PN} += "${sysconfdir}/init.d"
FILES:${PN} += "${sysconfdir}/rc*.d"

do_configure () {
	:
}

do_compile () {
	oe_runmake
}

do_install () {
	# TODO: Install your binaries/scripts here.
	# Be sure to install the target directory with install -d first
	# Yocto variables ${D} and ${S} are useful here, which you can read about at 
	# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-D
	# and
	# https://docs.yoctoproject.org/ref-manual/variables.html?highlight=workdir#term-S
	# See example at https://github.com/cu-ecen-aeld/ecen5013-yocto/blob/ecen5013-hello-world/meta-ecen5013/recipes-ecen5013/ecen5013-hello-world/ecen5013-hello-world_git.bb

	
	install -d ${D}${bindir}
	install -m 0755 ${S}/aesdsocket ${D}${bindir}

	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${S}/aesdsocket-start-stop ${D}${sysconfdir}/init.d/

	install -d ${D}${sysconfdir}/rc0.d
	install -d ${D}${sysconfdir}/rc1.d
	install -d ${D}${sysconfdir}/rc5.d
	install -d ${D}${sysconfdir}/rc6.d
	ln -sf ../init.d/aesdsocket-start-stop ${D}${sysconfdir}/rc5.d/S99aesdsocket
    ln -sf ../init.d/aesdsocket-start-stop ${D}${sysconfdir}/rc6.d/K99aesdsocket
	ln -sf ../init.d/aesdsocket-start-stop ${D}${sysconfdir}/rc1.d/K99aesdsocket
	ln -sf ../init.d/aesdsocket-start-stop ${D}${sysconfdir}/rc0.d/K99aesdsocket
}

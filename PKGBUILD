# Maintainer: Your Name <your.email@example.com>
pkgname=plantuml-generator
pkgver=1.0.0
pkgrel=1
pkgdesc="A tool to generate PlantUML diagrams from Java source code"
arch=('any')
url="https://github.com/yourusername/plantuml-generator"
license=('MIT') # Assuming MIT, adjust as needed
depends=('java-runtime>=21')
makedepends=('java-environment>=21')
source=("file://$(pwd)") # Local source for development convenience
md5sums=('SKIP')

prepare() {
    cd "$srcdir/$pkgname"
    # Ensure gradlew is executable
    chmod +x gradlew
}

build() {
    cd "$srcdir/$pkgname"
    ./gradlew :lib:standaloneJar --no-daemon
}

package() {
    cd "$srcdir/$pkgname"

    # Install the Fat JAR
    install -Dm644 lib/build/libs/lib-1.0.0-all.jar "$pkgdir/usr/share/java/$pkgname/$pkgname.jar"

    # Create a startup script
    mkdir -p "$pkgdir/usr/bin"
    echo "#!/bin/sh" > "$pkgdir/usr/bin/$pkgname"
    echo "exec java -jar /usr/share/java/$pkgname/$pkgname.jar \"\$@\"" >> "$pkgdir/usr/bin/$pkgname"
    chmod 755 "$pkgdir/usr/bin/$pkgname"
}

## About
This is a terminal app for math expressions calculation.
It allows to make calculation fast right in your console, like:
```
calc "5.4 * 3,000.02 - 5*2 + 3! - (4+5) + sin 0 - cos PI"
```

## Compilation
### MacOS
Make sure you have java and xcode installed.
Run the following command in terminal `./gradlew :macosBinaries`
After compilation you will find your executable in `/build/bin/macos/releaseExecutable`

### Linux
Make sure you have java.
Run the following command in terminal `./gradlew :linuxBinaries`
After compilation you will find your executable in `/build/bin/linux/releaseExecutable`

### Windows
Make sure you have java.
Run the following command in terminal `./gradlew :windowsBinaries`
After compilation you will find your executable in `/build/bin/windows/releaseExecutable`

## Installation
Make sure your executable is reachable in PATH. 
In case it is not you have 2 choices:
* You can move it to some of the directories from PATH (for example `/usr/local/bin/`)
* You can add the folder where executable is located to your PATH

> feel free to rename executable to make it more convenient for you

## Applicable operators
This app can process the following operators (ordered by priority):
1. `(`,`)`
2. `!` (factorial), `^` (power), `v` (square root) 
3. `ln` (logarithm base e), `log` (logarithm base 10), `sin`, `cos`, `tg`/`tan`
4. `*`/`x`, `/`
5. `-`, `+`

## Applicable constants
* PI
* e

## Expression examples
In simple cases you don't need to put brackets. 
The app will try to parse an expression using the predefined priority of operators.

* `calc "5 + 6 * 2"` will be parsed like `5+(6*2)`
* `calc "5 (6 * 2)"` will be parsed like `5*(6*2)`
* `calc "sin cos 0"` will be parsed like `sin(cos(0))`
* `calc "5 + 4 * 2!"` will be parsed like `5+(4*(2!))`

If you not sure about priority fill free to use brackets:
* `calc "(5 + 6) * 2"` will be parsed like `(5+6)*2`
* `calc "((v3) * 2) / (5!)"`
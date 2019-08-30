object ASCII {
  /// https://en.wikipedia.org/wiki/ANSI_escape_code

  val CSI = "\u001B["
  val safePos: String = CSI + "s"
  val restorePos: String = CSI + "u"
  val hideCursor: String = CSI + "?25l"
  val showCursor: String = CSI + "?25h"
  val clearScreen: String = CSI + "1J"

  def setCursor(x: Int = 0, y: Int = 0): String = CSI + y + ";" + x + "H"

  def curUp(x: Int = 1): String = CSI + x + "A"
  def curDown(x: Int = 1): String = CSI + x + "B"
  def curFwd(x: Int = 1): String = CSI + x + "C"
  def curBack(x: Int = 1): String = CSI + x + "D"
  def scrollUp(x : Int = 1): String = CSI + x + "S"
  def scrollDown(x: Int = 1) : String = CSI + x + "T"

  /// SGR
  def SGR(code : Int) : String = CSI + code + "m"
  val RESET = SGR(0)
  // Does not seem to work?
  val BOLD = SGR(1)
  val ITALIC = SGR(3)
  var BLINK = SGR(5)

  // 8 bit color support
  def FG8(colorIndex : Int) = CSI + "38;5;" + colorIndex + "m"
  def BG8(colorIndex : Int) = CSI + "48;5;" + colorIndex + "m"

  // 24-bit color support (works in iTerm2 but not in Terminal.app)
  def FG(r : Int, g: Int, b : Int) = CSI + "38;2;" + r + ";" + g + ";" + b + "m"
  def BG(r : Int, g: Int, b : Int) = CSI + "48;2;" + r + ";" + g + ";" + b + "m"

}

object JumpRun extends App {
  // Unicode Chars copied from this page:
  // https://unicode-table.com/en/
  val seq: List[Char] = List('\u23BA', '\u23BB', '\u23BC', '\u23BD')
  var idx = 0

  var frames: Int = 0
  var beginFrame = 0.0f
  var endFrame = 0.0f
  var deltaTime = 0.0f
  var exit = false;


  // Startup
  print(ASCII.hideCursor)
  // clear screen is very slow  and flickers inside the event loop
  print(ASCII.clearScreen)

  while (!exit) {
    beginFrame = System.nanoTime
    Update()
    endFrame = System.nanoTime

    deltaTime += endFrame - beginFrame;
    frames += 1;
  }

  // before exit
  print(ASCII.showCursor)


  /// Main render loop, called every frame
  def Update(): Unit = {
    print(ASCII.setCursor())
    println("Type q key to exit")

    print(ASCII.curDown(3))
    print(seq(idx))
    idx += 1
    if (idx == seq.length) idx = 0

    print(ASCII.setCursor(30, 0))
    print("FPS" + deltaTime / frames / 1000)

    print(ASCII.setCursor(15,15))
    print(ASCII.FG(255,255,0))
    print(ASCII.BG(0,255,128))
    print("Test string with 24-bit colors")
    print(ASCII.setCursor(15,16))
    print(ASCII.FG8(4))
    print(ASCII.BG8(12))
    print("Test string with 8-bit colors")
    print(ASCII.RESET)

    if (System.in.available > 0) {
      val key = System.in.readNBytes(1)
      // the usual ascii key codes are used
      if (key.nonEmpty && key(0) == 113) exit = true
    }
  }

}

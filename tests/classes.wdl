VAR_POP = "Эта переменная из classes"

class Point {

  def Point(x = 0, y = 0) {
    this.x = x
    this.y = y
  }

  def move(dx, dy) {
    this.x += dx
    this.y += dy
  }

  def toString() = "(" + this.x + ", " + this.y + ")  " + VAR_POP
}

p = new Point(20, 30)
p.move(10, -5)
println(p.toString())


p2 = new Point(10, -5)
p2.move(10, -5)
println(p2.toString())
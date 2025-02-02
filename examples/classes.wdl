class Point {
  def Point(x = 0, y = 0) {
    this.x = x
    this.y = y
  }

  def moveBy(p) {
     this.move(p.x, p.y)
  }

  def move(dx, dy) {
    this.x += dx
    this.y += dy
  }

  def toString() = "(" + this.x + ", " + this.y + ")"
}

p = new Point(20, 30)
p.move(10, -5)
println(p.toString())

p2 = new Point(1, 1)
p2.moveBy(p)
println(p2.toString())
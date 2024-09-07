func()
def func() println("Function!")

println(3 + 10)

a = 1
b = 0

println(a ? "True" : "False")
println(b ? "True" : "False")
println(!1)
println(!0)
println(1^2)

sum = def (a, b) return a + b
div = def (a, b) {
    println(a)
    println(b)
    return a / b
}

println("sum = " + sum(10, 20))
println("div = " + div(10, 20))

map = {"key": "val1", "key2": 1234}
println(map["key"])
map["key2"] = "val2"
println(map["key2"])

run(::af)

def af(){
    println("AF")
}

def run(_func){
    _func()
}

arr = [1,2,3,4]
arrSum = [6, 7, 8]
println(arr)
arr = arr::5
println(arr)
arr = arr << arrSum
println(arr)

mathFunc = def (var) = match var {
    case 4: "Tut 4!!!"
    case other: "Other: " + other
}

def fact(n) = match n {
    case 0 : 1
    case _ : n * fact(n - 1)
}
println(fact(10))

for(a : arr){
    println("for a = " + a)
    println("for a = func " + mathFunc(a))
}

map["f"] = ::fact

for(k, v : map){
    println("for map = " + k + ": " + v)
}

classss = {
  "val": 123,
  "add": def(a, b) return a + b,
  "sub": def(a, b) = a - b,
  "mul": def(a, b) = a * b,
  "div": def(a, b) = a / b
}

println(classss["add"](2, classss["sub"](2, 2)))

mTest = "Ttt"

match mTest {
    case "awd": println("daw")
    case "ttT": println("awdawd")
    case "Ttt": println("Good!")
    case _: println("NOT!")
}

def f1(){
    def f2(){
        println(map.f(10))
    }
    f2()
}
f1()
f2()


def funcWithOptionalArgs(str, count = 5, prefix = "<", suffix = ">") = prefix + (str * count) + suffix

println(funcWithOptionalArgs("*"))
println(funcWithOptionalArgs("+", 2))
println(funcWithOptionalArgs("*", 10, "<!"))
println()

println(10 + 20 * 2 * 10 / 4)

for (i = 0.5, i < 10, i++){
    println("new += for i " + i)
}

class TestClass{
    def add(a, b) = a + b
    def assers(a, b) = assertEquals(a, b)
    def runTest(){
        this.assers(4, this.add(2, 3))
    }
}

testClass = new TestClass()
testClass.runTest()

def f1() = {"func": ::f2}
def f2() = {
  "functions" : {
    "add" : def(a, b) = a + b
    "mul" : def(a, b) = a * b
    "negate" : def(a) = {"result" : -a}
    "as": def(a, b) = assertEquals(a, b)
  }
}
def f3() = def() = def() = def() = "test"
def f4() = def() = ::f1

def testFunctionChain() {
  assertEquals(5, f1().func().functions.add(2, 3))
  assertEquals(6, f1().func().functions.mul(2, 3))
}

def testCallChain() {
  assertEquals("test", f3()()()())
}

def testBoth(a, b, c=23) {
  f4()()().func().functions.as(-123, f4()()().func().functions.negate(-123).result)
}


testFunctionChain()
testCallChain()
testBoth(10, 2)

assertEquals(10, "1234567890".length)

eee= 3E+10 / 1000000000
println(eee)
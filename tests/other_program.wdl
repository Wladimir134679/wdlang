println ("Start test other program")

println("Create function")
def testFunc(args, a = 10, b = 15){
    println("Вызов функции: testFunc, args = " + args + " a = " + a + " b = " + b)
    return a * b
}
println("Execute function testFunc = " + testFunc("ARGSSTRING", 2))

println("End test other program")
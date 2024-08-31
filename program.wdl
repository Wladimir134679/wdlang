word = 2 + 2
word2 = PI + word
print word
print "\n"
/*
dwad
dawda
wdawd
*/
// print "Tut text Типо вцфвфцв\n"
print "ToT" * 5 + "\n"
print "Hello" + "World\n"
print 5 == 5
print "\n"

// Проверка условий
if (1 <= 2) print "1 = 1"
else print "1 != 1"
print "\n"
print sin(PI/2)

if(40 < 50 && 50 <= 60) {
    print "true \n"
    print "block\n"
    i = 0
    while (i < 10) {
        println("i = " + i)
        i = i + 1
    }
    for i = 0, i < 10, i = i + 1 {
        if i == 4 continue
        println("i = " + i)
    }
}
i = 0
do {
    println("do i = " + i + " sin=" + sin(i))
    i = i + 1
} while(i < 10)

a = "PRINT"
println("a=" + a)

def test(a, b){
    println("Def test, a=" + a + ", b=" + b)
}
def sum(a, b){
    return a + b
}

println("a=" + a)
test("A", 125432)
println("a=" + a)
println(sum(10, 5))

arr = ["daw", sum(5, 5), "WOLF", [], [1, 2, 3, 3 + 1]]
println(arr + "\n")
arr[0] = 100
println(arr * 4)
println(arr[0] + arr[1])
println(arr[4][0])
arr[4][0] = 124
println(arr[4][0])
testArr = newarray(2, 3, 2)
println(testArr)

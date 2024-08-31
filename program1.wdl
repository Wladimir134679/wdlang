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
div = def (a, b) = a / b

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

for(a : arr)
    println("for a = " + a)

for(k, v : map)
    println("for map = " + k + ": " + v)


println "start"

class C1 {
    var1 = 45
    def getName(){
        return "Name1"
    }

    def getMap(){
        return {"key1": "Value152", "key2": 5, "key3": [1, 2, 6]}
    }
}

c = new C1()

println c.var1
println c.getMap()
println c.getMap().key1
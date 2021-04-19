
println "start"

variable = 2

class C2 {
    var1 = 64573347
    def getName(){
        return "NameClass"
    }

    def getMap(){
        variable = 7
        return {"key1": "Value1421", "key2": 57, "key3": [14, 2, 3]}
    }
}

c = new C2()

println variable
println c.var1
println c.getMap()
println c.getMap().key1
println variable
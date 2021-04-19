use ["a", "b"]

class Main{

    def main(args){
        println("ASD")
        test = new Test()
        println(test.sum(3))
    }
}

class Test{
    var1 = 12
    def sum(a){
        return var1 + a
    }
}
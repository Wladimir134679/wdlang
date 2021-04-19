class C1{
    var1 = "Var1"

    def getVar(){
        return var1 + "Class"
    }
}

def init(){
    println "InitProgram!"
    use "std"
    setTmp(1)
}

def main(){
    println "MainProgram!"
    println "Tmp: " + getTmp()

    c1 = new C1()
    println c1.getVar()
}
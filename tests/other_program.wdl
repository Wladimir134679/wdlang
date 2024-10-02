import logger as log

log.init("other program", "${date} [${thread}] ${level} ${module} - \t ${message}")

log.info("Start test other program")

log.info("Create function")
def testFunc(args, a = 10, b = 15){
    log.info("Вызов функции: testFunc, args = " + args + " a = " + a + " b = " + b)
    return a * b
}
log.info("Execute function testFunc = " + testFunc("ARGSSTRING", 2))

log.info("End test other program")
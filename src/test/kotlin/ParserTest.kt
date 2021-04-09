import exceptions.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ParserTest {
    val parse = Parser.Companion::evaluate

    @Test
    fun testExample() {
        assertEquals(4, parse("(2+2)"))
        assertEquals(4, parse("(2+((3*4)/5))"))
        assertEquals(0, parse("[((10+20)>(20+10))]?{1}:{0}"))
        assertEquals(60, parse("g(x)={(f(x)+f((x/2)))}\n" +
                                             "f(x)={[(x>1)]?{(f((x-1))+f((x-2)))}:{x}}\n" +
                                             "g(10)"))
        val syntaxException = assertFailsWith<SyntaxException> { parse("1 + 2 + 3 + 4 + 5") }
        assertEquals("SYNTAX ERROR", syntaxException.message)
        val parameterNotFoundException = assertFailsWith<ParameterNotFountException> { parse("f(x)={y}\nf(10)") }
        assertEquals("PARAMETER NOT FOUND y:1", parameterNotFoundException.message)
        val functionNotFountException = assertFailsWith<FunctionNotFoundException> { parse("g(x)={f(x)}\ng(10)") }
        assertEquals("FUNCTION NOT FOUND f:1", functionNotFountException.message)
        val argumentNumberException = assertFailsWith<ArgumentNumberException> { parse("g(x)={(x+1)}\ng(10,20)") }
        assertEquals("ARGUMENT NUMBER MISMATCH g:2", argumentNumberException.message)
        val runtimeException = assertFailsWith<ParserRuntimeException> { parse("g(a,b)={(a/b)}\ng(10,0)") }
        assertEquals("RUNTIME ERROR (a/b):1", runtimeException.message)
    }

    @Test
    fun testEmpty() {
        assertFailsWith<SyntaxException>{ parse("") }
    }

    @Test
    fun testConst() {
        assertEquals(2, parse("2"))
        assertEquals(0, parse("0"))
        assertEquals(123, parse("123"))
        assertFailsWith<SyntaxException> { parse("123a") }
        assertFailsWith<SyntaxException> { parse("12a3") }
    }

    @Test
    fun testVariable() {
        assertFailsWith<ParameterNotFountException> { parse("x") }
        assertFailsWith<ParameterNotFountException> { parse("hello") }
    }

    @Test
    fun testBinary() {
        assertEquals(0, parse("(0+0)"))
        assertEquals(0, parse("(0-0)"))
        assertEquals(10, parse("(5+5)"))
        assertFailsWith<SyntaxException> { parse("(10+20") }
        assertEquals(1, parse("((10*2)=(30-10))"))
        assertEquals(0, parse("(5>5)"))
    }

    @Test
    fun testIf() {
        assertEquals(42, parse("[(1=1)]?{42}:{41}"))
        assertEquals(5, parse("(2+[(1>2)]?{(1+1)}:{(6/2)})"))
        assertFailsWith<SyntaxException> { parse("[1]{10}{11}") }
    }

    @Test
    fun testFunction() {
        assertEquals(10, parse("f(x)={x}\nf(10)"))
        val fibCode = "fib(n)={[(n>1)]?{(fib((n-1))+fib((n-2)))}:{1}}\n"
        fun fib(n: Int): Int {
            return if (n > 1) {
                fib(n - 1) + fib(n - 2)
            } else {
                1
            }
        }

        for (i in 1..20) {
            assertEquals(fib(i), parse(fibCode + "fib(${i})"))
        }

        assertFailsWith<FunctionNotFoundException> { parse("f(10)") }
        assertFailsWith<FunctionNotFoundException> { parse("f(x)={g(x)}\nf(10)") }

        assertFailsWith<ArgumentNumberException> { parse("f(x)={x}\nf(1,2)") }
        assertFailsWith<ArgumentNumberException> { parse("f(x,y)={(x+y)}\nf(10)") }
    }

    @Test
    fun testRuntime() {
        assertFailsWith<ParserRuntimeException> { parse("(1/0)") }
        assertEquals(10, parse("[1]?{10}:{(1/0)}"))
        assertFailsWith<ParserRuntimeException> { parse("f(x,y,z)={((x+y)/z)}\nf(1,-1,0)") }
    }
}
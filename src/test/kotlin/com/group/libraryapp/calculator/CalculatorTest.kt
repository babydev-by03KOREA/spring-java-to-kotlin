package com.group.libraryapp.calculator

fun main() {
    val calculator = CalculatorTest()
    calculator.addTest()
    calculator.minusTest()
    calculator.multiplyTest()
    calculator.divideTest()
    calculator.divideExceptionTest()
}

class CalculatorTest {
    fun addTest() {
        // given, 다음을 테스트 하기 위해 준비하는 것
        val calculator = Calculator(5)

        // when, 우리가 테스트 할 기능을 호출하는 것
        calculator.add(3)

        // then, 호출을 했더니 다음과 같은 결과가 나온 것
        if (calculator.number != 8) {
            throw IllegalArgumentException()
        }
    }

    fun minusTest() {
        // given, 다음을 테스트 하기 위해 준비하는 것
        val calculator = Calculator(5)

        // when, 우리가 테스트 할 기능을 호출하는 것
        calculator.minus(3)

        // then, 호출을 했더니 다음과 같은 결과가 나온 것
        if (calculator.number != 2) {
            throw IllegalArgumentException()
        }
    }

    fun multiplyTest() {
        // given, 다음을 테스트 하기 위해 준비하는 것
        val calculator = Calculator(5)

        // when, 우리가 테스트 할 기능을 호출하는 것
        calculator.multiply(3)

        // then, 호출을 했더니 다음과 같은 결과가 나온 것
        if (calculator.number != 15) {
            throw IllegalArgumentException()
        }
    }

    fun divideTest() {
        // given, 다음을 테스트 하기 위해 준비하는 것
        val calculator = Calculator(5)

        // when, 우리가 테스트 할 기능을 호출하는 것
        calculator.divide(2)

        // then, 호출을 했더니 다음과 같은 결과가 나온 것
        if (calculator.number != 2) {
            throw IllegalArgumentException()
        }
    }

    fun divideExceptionTest() {
        // given, 다음을 테스트 하기 위해 준비하는 것
        val calculator = Calculator(5)

        // when, 우리가 테스트 할 기능을 호출하는 것
        try {
            calculator.divide(0)
        } catch (e: IllegalArgumentException) {
            if (e.message != "0으로 나눌 수 없습니다.") {
                throw IllegalStateException("메시지가 다릅니다.")
            }
            // TEST SUCCESS!
            return
        } catch (e: Exception) {
            throw IllegalStateException()
        }
        throw IllegalStateException("기대하는 예외가 발생하지 않았습니다.")
    }
}
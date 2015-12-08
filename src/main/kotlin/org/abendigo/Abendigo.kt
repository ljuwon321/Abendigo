package org.abendigo

import co.paralleluniverse.fibers.*
import co.paralleluniverse.kotlin.fiber
import co.paralleluniverse.strands.Strand
import org.abendigo.csgo.m_dwLocalPlayer
import org.jire.kotmem.*
import java.util.concurrent.TimeUnit

@Suspendable
fun sleep(duration: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS) = Strand.sleep(duration, timeUnit)

@Suspendable
fun sleep(duration: Int, timeUnit: TimeUnit = TimeUnit.MILLISECONDS) = sleep(duration.toLong(), timeUnit)

@Suspendable
inline fun <T> every(duration: Int, timeUnit: TimeUnit = TimeUnit.MILLISECONDS, crossinline action: () -> T):
		Fiber<Unit> = every(duration.toLong(), timeUnit, action)

@Suspendable
inline fun <T> every(duration: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS, crossinline action: () -> T) =
		fiber {
			do {
				action()
				sleep(duration, timeUnit)
			} while (!Strand.interrupted())
		}

open class UpdateableLazy<T>(private val lazy: () -> T) {

	private var current: T? = null
	private var previous: T? = null

	operator fun invoke(): T {
		if (current == null) +this
		return current!!
	}

	operator fun unaryPlus(): T {
		previous = current
		current = lazy()
		return current!!
	}

	operator fun unaryMinus(): T {
		current = previous ?: return this()
		return current!!
	}

}

object keys {
	operator fun get(vKey: Int) = isKeyDown(vKey)
}

fun main(args: Array<String>) {
	/*every(1000) {
		println("kanyewest")
	}*/
	/*BunnyHopPlugin().enable()*/
	val csgo = processes["csgo.exe"]
	val client = csgo["client.dll"]
	val me: Int = client[m_dwLocalPlayer]
	Thread.sleep(Long.MAX_VALUE)
}
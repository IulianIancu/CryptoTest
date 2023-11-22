package com.iulian.iancu.domain

class GetUserWalletUseCase {
    private fun run(): Float {
        //This should call something to get a actual number, but for this app we'll just return a constant value
        return 10.0f
    }

    operator fun invoke(): Float {
        return run()
    }
}
package com.udacity


sealed class ButtonState {
    object Idle : ButtonState()
    object Clicked : ButtonState()
    object Loading : ButtonState()
    object Completed : ButtonState()
}
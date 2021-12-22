package com.example.editory

import android.view.MotionEvent
import android.view.View

object MovableViewOnTouch {
    /**
     * This function will enable moving a view with the
     * boundary limit provided by another view
     */
    fun moveOnTouchWithBoundary(viewToBeMoved: View, boundaryView: View){

        viewToBeMoved.setOnTouchListener (object : View.OnTouchListener {
            private var lastX = 0
            private var lastY = 0
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        lastX = event.rawX.toInt()
                        lastY = event.rawY.toInt()
                        v.performClick()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val dx = event.rawX.toInt() - lastX
                        val dy = event.rawY.toInt() - lastY
                        var left = v.left + dx
                        var top = v.top + dy
                        var right = v.right + dx
                        var bottom = v.bottom + dy
                        if (left < boundaryView.left) {
                            left = boundaryView.left
                            right = left + v.width
                        }
                        if (right > boundaryView.right) {
                            right = boundaryView.right
                            left = right - v.width
                        }
                        if (top < boundaryView.top) {
                            top = boundaryView.top
                            bottom = top + v.height
                        }
                        if (bottom > boundaryView.bottom) {
                            bottom = boundaryView.bottom
                            top = bottom - v.height
                        }
                        println("$left $right $top $bottom")
                        v.layout(left, top, right, bottom)
                        lastX = event.rawX.toInt()
                        lastY = event.rawY.toInt()
                    }
                }
                return true
            }
        })

    }
}
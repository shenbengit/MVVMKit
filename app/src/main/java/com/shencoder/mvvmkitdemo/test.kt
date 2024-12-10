class RootViewGestureDetector constructor(private val rootView: ViewGroup) {

    private companion object {
        private const val TAG = "RootViewGestureDetector"
        private const val MIN_SCALE = 1.0f
        private const val MAX_SCALE = 3.0f
    }

    private val displayRectF = RectF()
    private val viewRectF = RectF()


    var minScale = MIN_SCALE
    var maxScale = MAX_SCALE

    private var currentScale = minScale

    var onGestureCallbackListener: OnGestureCallbackListener? = null

    private val listener = object : OnGestureListener {

        override fun onDrag(dx: Float, dy: Float) {
            if (isInProgress) {
                return
            }
            val scale = currentScale
            var offsetX = dx * scale
            var offsetY = dy * scale

            if (displayRectF.left + dx > viewRectF.left) {
                offsetX = viewRectF.left - displayRectF.left
            } else if (displayRectF.right + dx < viewRectF.right) {
                offsetX = viewRectF.right - displayRectF.right
            }

            if (displayRectF.top + dy > viewRectF.top) {
                offsetY = viewRectF.top - displayRectF.top
            } else if (displayRectF.bottom + dy < viewRectF.bottom) {
                offsetY = viewRectF.bottom - displayRectF.bottom
            }

            displayRectF.offset(offsetX, offsetY)

            rootView.translationX += offsetX
            rootView.translationY += offsetY

//            Log.d(
//                TAG,
//                "onDrag: dx: $dx, dy: $dy, scale: $scale, translationX: ${rootView.translationX}, translationY: ${rootView.translationY}, displayRectF: $displayRectF, width: ${displayRectF.width()}, height: ${displayRectF.height()}"
//            )
        }

        override fun onFling(startX: Float, startY: Float, velocityX: Float, velocityY: Float) {
        }

        override fun onScaleBegin() {
            onGestureCallbackListener?.onScaleBegin()
        }

        override fun onScale(
            scaleFactor: Float, focusX: Float, focusY: Float,
            currentSpan: Float,
            previousSpan: Float
        ) {
            var scale = currentScale
            if (scale <= minScale && /*小于1，代表缩小*/scaleFactor < 1f) {
                return
            }
            if (scale >= maxScale && /*大于1，代表放大*/scaleFactor > 1f) {
                return
            }
            var factory = scaleFactor
            if (scale * scaleFactor > maxScale) {
                factory = maxScale / scale
            } else if (scale * scaleFactor < minScale) {
                factory = minScale / scale
            }

            currentScale *= factory

            scale = min(maxScale, max(minScale, currentScale))

            if (scale == minScale) {
                reset(false)
            } else {
                // 以原始view中心点进行缩放，计算缩放后的大小
                val left = viewRectF.left * scale
                val top = viewRectF.top * scale
                val right = viewRectF.right * scale
                val bottom = viewRectF.bottom * scale

                val width = right - left
                val height = bottom - top

                val dx = (displayRectF.width() - width) / 2
                val dy = (displayRectF.height() - height) / 2
                displayRectF.inset(dx, dy)

//                Log.i(
//                    TAG,
//                    "onScale111: scale: $scale, translationX: ${rootView.translationX}, translationY: ${rootView.translationY}, displayRectF: $displayRectF, width: ${displayRectF.width()}, height: ${displayRectF.height()}"
//                )
var offsetX = 0f
                var offsetY = 0f

                if (displayRectF.left > viewRectF.left) {
                    offsetX = viewRectF.left - displayRectF.left
                } else if (displayRectF.right < viewRectF.right) {
                    offsetX = viewRectF.right - displayRectF.right
                }

                if (displayRectF.top > viewRectF.top) {
                    offsetY = viewRectF.top - displayRectF.top
                } else if (displayRectF.bottom < viewRectF.bottom) {
                    offsetY = viewRectF.bottom - displayRectF.bottom
                }

                rootView.translationX += offsetX
                rootView.translationY += offsetY
                displayRectF.offset(offsetX, offsetY)

//                Log.i(
//                    TAG,
//                    "onScale222: scale: $scale, translationX: ${rootView.translationX}, translationY: ${rootView.translationY}, displayRectF: $displayRectF, width: ${displayRectF.width()}, height: ${displayRectF.height()}"
//                )
                rootView.scaleX = scale
                rootView.scaleY = scale
            }

            onGestureCallbackListener?.onScale(scale, minScale, maxScale)
        }

        override fun onScaleEnd() {
            val scale = currentScale
            onGestureCallbackListener?.onScaleEnd(scale, scale <= minScale, scale >= maxScale)
        }
    }

    private val scaleDragDetector = CustomScaleGestureDetector(rootView.context, listener)
    private val gestureDetector =
        GestureDetector(rootView.context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                reset(true)
                return true
            }
        })

    init {
        rootView.post {
            viewRectF.set(
                0f,
                0f,
                getViewWidth(rootView).toFloat(),
                getViewHeight(rootView).toFloat()
            )
            displayRectF.set(viewRectF)
        }
    }


    val isInProgress: Boolean
        get() = scaleDragDetector.isInProgress

    fun reset(manual: Boolean) {
        val forceUpdate = currentScale.compareTo(minScale) != 0
        if (forceUpdate) {
            LogUtils.i(TAG,"reset forceUpdate.")
        }

        currentScale = minScale
        rootView.translationX = 0f
        rootView.translationY = 0f
        rootView.scaleX = minScale
        rootView.scaleY = minScale
        displayRectF.set(viewRectF)
        if (forceUpdate || manual) {
            onGestureCallbackListener?.onScaleEnd(
                currentScale,
                isMinimize = true,
                isMaximize = false
            )
        }
    }

    fun onTouch(event: MotionEvent): Boolean {
        scaleDragDetector.onTouchEvent(event)
        return gestureDetector.onTouchEvent(event)
    }

    private fun getViewWidth(view: View): Int {
        return view.width - view.paddingStart - view.paddingEnd
    }

    private fun getViewHeight(view: View): Int {
        return view.height - view.paddingTop - view.paddingBottom
    }

    private class CustomScaleGestureDetector(
        private val context: Context,
        private val listener: OnGestureListener
    ) {
        private val scaleGestureDetector = ScaleGestureDetector(context,
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

                override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                    listener.onScaleBegin()
                    return super.onScaleBegin(detector)
                }

                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    val scaleFactor = detector.scaleFactor
                    if (scaleFactor.isNaN() || scaleFactor.isInfinite()) return false
                    if (scaleFactor >= 0f) {
                        listener.onScale(
                            scaleFactor,
                            detector.focusX,
                            detector.focusY,
                            detector.currentSpan,
                            detector.previousSpan
                        )
                    }
                    return false
                }

                override fun onScaleEnd(detector: ScaleGestureDetector) {
                    listener.onScaleEnd()
                }

            })
        private val touchSlop: Int
        private var isDragging = false

        private val touchArray = SparseArray<Pair<Float, Float>>()

        init {
            val configuration = ViewConfiguration.get(context)
            touchSlop = configuration.scaledTouchSlop
        }

        val isInProgress: Boolean
            get() = scaleGestureDetector.isInProgress

        fun onTouchEvent(event: MotionEvent): Boolean {
            /*always return true*/scaleGestureDetector.onTouchEvent(event)
            return processTouchEvent(event)
        }

        private fun processTouchEvent(event: MotionEvent): Boolean {
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    isDragging = false
                    val pointerId = event.getPointerId(event.actionIndex)
                    touchArray[pointerId] = event.getX(0) to event.getY(0)
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    touchArray.clear()
                }

                MotionEvent.ACTION_MOVE -> {
                    val pointerCount = event.pointerCount
                    if (pointerCount >= 2) {
                        // 双指

                        val x0 = event.getX(0)
                        val y0 = event.getY(0)

                        val x1 = event.getX(1)
                        val y1 = event.getY(1)

                        val centerXNew = (x0 + x1) / 2
                        val centerYNew = (y0 + y1) / 2

                        val pointerId0 = event.getPointerId(0)
                        val pointerId1 = event.getPointerId(1)
                        val pair0 = touchArray[pointerId0]
                        val pair1 = touchArray[pointerId1]

                        // https://apm.umeng.com/platform/61c1c7d5e014255fcbc24ed9/error_analysis/crash/detail/9502466033217?errorId=9502466033217
                        // https://apm.umeng.com/platform/61c1c7d5e014255fcbc24ed9/error_analysis/crash/detail/9495614821217?errorId=9495614821217
                        // 友盟上上报空指针的问题，可能是之前 !! 引起的，这里判空一下
                        if (pair0 == null || pair1 == null) {
                            return true
                        }

                        val centerXOld = (pair0.first + pair1.first) / 2
                        val centerYOld = (pair0.second + pair1.second) / 2

                        val dx = centerXNew - centerXOld
                        val dy = centerYNew - centerYOld

                        if (!isDragging) {
                            isDragging = sqrt(dx * dx + dy * dx) >= touchSlop
                        }
                        if (isDragging) {
                            listener.onDrag(dx, dy)
                        }
                    }
                }

                MotionEvent.ACTION_POINTER_DOWN -> {
                    val actionIndex = event.actionIndex
                    val pointerId = event.getPointerId(actionIndex)
                    touchArray[pointerId] = event.getX(actionIndex) to event.getY(actionIndex)
                }

                MotionEvent.ACTION_POINTER_UP -> {
                    val pointerId = event.getPointerId(event.actionIndex)
                    touchArray.remove(pointerId)
                }
            }
            return true
        }

    }

    interface OnGestureListener {
        fun onDrag(dx: Float, dy: Float)

        fun onFling(
            startX: Float, startY: Float, velocityX: Float,
            velocityY: Float
        )

        fun onScaleBegin()
        fun onScale(
            scaleFactor: Float,
            focusX: Float,
            focusY: Float,
            currentSpan: Float,
            previousSpan: Float
        )

        fun onScaleEnd()
    }

    interface OnGestureCallbackListener {
        fun onScaleBegin()
        fun onScale(scale: Float, minScale: Float, maxScale: Float)
        fun onScaleEnd(scale: Float, isMinimize: Boolean, isMaximize: Boolean)
    }
}
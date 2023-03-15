package com.koonny.wheelview.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.Handler
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.Scroller
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import androidx.annotation.Px
import androidx.annotation.StyleRes
import com.koonny.wheelview.R
import com.koonny.wheelview.annotation.CurtainCorner
import com.koonny.wheelview.annotation.ItemTextAlign
import com.koonny.wheelview.annotation.ScrollState
import com.koonny.wheelview.contract.OnWheelChangedListener
import com.koonny.wheelview.contract.TextProvider
import com.koonny.wheelview.contract.WheelFormatter

class WheelView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.WheelStyle) :
    View(context, attrs, defStyleAttr), Runnable {

    private var data = mutableListOf<Any>()
    private var formatter: WheelFormatter? = null
    private var defaultItem: Any? = null
    private var visibleItemCount = 0
    private var defaultItemPosition = 0
    var currentPosition = 0
        protected set
    private var maxWidthText: String? = null
    private var textColor = 0
    private var selectedTextColor = 0
    private var textSize = 0f
    private var selectedTextSize = 0f
    private var selectedTextBold = false
    private var indicatorSize = 0f
    private var indicatorColor = 0
    private var curtainColor = 0
    private var curtainCorner = 0
    private var curtainRadius = 0f
    private var itemSpace = 0
    private var textAlign = 0
    private var sameWidthEnabled = false
    private var indicatorEnabled = false
    private var curtainEnabled = false
    private var atmosphericEnabled = false
    private var cyclicEnabled = false
    private var curvedEnabled = false
    private var curvedMaxAngle = 90
    private var curvedIndicatorSpace = 0
    private val handler = Handler()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG or Paint.LINEAR_TEXT_FLAG)
    private val scroller: Scroller
    private var tracker: VelocityTracker? = null
    private var onWheelChangedListener: OnWheelChangedListener? = null
    private val rectDrawn = Rect()
    private val rectIndicatorHead = Rect()
    private val rectIndicatorFoot = Rect()
    private val rectCurrentItem = Rect()
    private val camera = Camera()
    private val matrixRotate = Matrix()
    private val matrixDepth = Matrix()
    private var lastScrollPosition = 0
    private var drawnItemCount = 0
    private var halfDrawnItemCount = 0
    private var textMaxWidth = 0
    private var textMaxHeight = 0
    private var itemHeight = 0
    private var halfItemHeight = 0
    private var halfWheelHeight = 0
    private var minFlingYCoordinate = 0
    private var maxFlingYCoordinate = 0
    private var wheelCenterXCoordinate = 0
    private var wheelCenterYCoordinate = 0
    private var drawnCenterXCoordinate = 0
    private var drawnCenterYCoordinate = 0
    private var scrollOffsetYCoordinate = 0
    private var lastPointYCoordinate = 0
    private var downPointYCoordinate = 0
    private val minimumVelocity: Int
    private val maximumVelocity: Int
    private val touchSlop: Int
    private var isClick = false
    private var isForceFinishScroll = false

    init {
        initAttrs(context, attrs, defStyleAttr, R.style.Widget_Koonny_WheelView)
        initTextPaint()
        updateVisibleItemCount()
        scroller = Scroller(context)
        val configuration = ViewConfiguration.get(context)
        minimumVelocity = configuration.scaledMinimumFlingVelocity
        maximumVelocity = configuration.scaledMaximumFlingVelocity
        touchSlop = configuration.scaledTouchSlop
        if (isInEditMode) {
            setData(generatePreviewData())
        }
    }

    private fun initTextPaint() {
        paint.color = textColor
        paint.textSize = textSize
        paint.isFakeBoldText = false
        paint.style = Paint.Style.FILL
    }

    fun setStyle(@StyleRes style: Int) {
        initAttrs(context, null, R.attr.WheelStyle, style)
        initTextPaint()
        updatePaintTextAlign()
        computeTextWidthAndHeight()
        computeFlingLimitYCoordinate()
        computeIndicatorRect()
        computeCurrentItemRect()
        requestLayout()
        invalidate()
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val density = context.resources.displayMetrics.density
        val scaledDensity = context.resources.displayMetrics.scaledDensity
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.WheelView, defStyleAttr, defStyleRes)
        visibleItemCount = typedArray.getInt(R.styleable.WheelView_wheel_visibleItemCount, 5)
        sameWidthEnabled = typedArray.getBoolean(R.styleable.WheelView_wheel_sameWidthEnabled, false)
        maxWidthText = typedArray.getString(R.styleable.WheelView_wheel_maxWidthText)
        textColor = typedArray.getColor(R.styleable.WheelView_wheel_itemTextColor, -0x777778)
        selectedTextColor = typedArray.getColor(R.styleable.WheelView_wheel_itemTextColorSelected, -0x1000000)
        textSize = typedArray.getDimension(R.styleable.WheelView_wheel_itemTextSize, 15 * scaledDensity)
        selectedTextSize = typedArray.getDimension(R.styleable.WheelView_wheel_itemTextSizeSelected, textSize)
        selectedTextBold = typedArray.getBoolean(R.styleable.WheelView_wheel_itemTextBoldSelected, false)
        textAlign = typedArray.getInt(R.styleable.WheelView_wheel_itemTextAlign, ItemTextAlign.CENTER)
        itemSpace = typedArray.getDimensionPixelSize(R.styleable.WheelView_wheel_itemSpace, (20 * density).toInt())
        cyclicEnabled = typedArray.getBoolean(R.styleable.WheelView_wheel_cyclicEnabled, false)
        indicatorEnabled = typedArray.getBoolean(R.styleable.WheelView_wheel_indicatorEnabled, true)
        indicatorColor = typedArray.getColor(R.styleable.WheelView_wheel_indicatorColor, -0x363637)
        indicatorSize = typedArray.getDimension(R.styleable.WheelView_wheel_indicatorSize, 1 * density)
        curvedIndicatorSpace = typedArray.getDimensionPixelSize(R.styleable.WheelView_wheel_curvedIndicatorSpace, (1 * density).toInt())
        curtainEnabled = typedArray.getBoolean(R.styleable.WheelView_wheel_curtainEnabled, false)
        curtainColor = typedArray.getColor(R.styleable.WheelView_wheel_curtainColor, -0x1)
        curtainCorner = typedArray.getInt(R.styleable.WheelView_wheel_curtainCorner, CurtainCorner.NONE)
        curtainRadius = typedArray.getDimension(R.styleable.WheelView_wheel_curtainRadius, 0f)
        atmosphericEnabled = typedArray.getBoolean(R.styleable.WheelView_wheel_atmosphericEnabled, false)
        curvedEnabled = typedArray.getBoolean(R.styleable.WheelView_wheel_curvedEnabled, false)
        curvedMaxAngle = typedArray.getInteger(R.styleable.WheelView_wheel_curvedMaxAngle, 90)
        typedArray.recycle()
    }

    protected fun generatePreviewData(): MutableList<Any> {
        val data = mutableListOf<Any>()
        data.add("贵州穿青人")
        data.add("大定府羡民")
        data.add("不在五十六个民族之内")
        data.add("已识别待定民族")
        data.add("穿青山魈人马")
        data.add("李裕江")
        return data
    }

    private fun updateVisibleItemCount() {
        val minCount = 2
        if (visibleItemCount < minCount) {
            throw ArithmeticException("Visible item count can not be less than $minCount")
        }
        //可见条目只能是奇数个，设置可见条目时偶数个将自动矫正为奇数个
        val evenNumberFlag = 2
        if (visibleItemCount % evenNumberFlag == 0) {
            visibleItemCount += 1
        }
        drawnItemCount = visibleItemCount + 2
        halfDrawnItemCount = drawnItemCount / 2
    }

    private fun computeTextWidthAndHeight() {
        textMaxHeight = 0
        textMaxWidth = textMaxHeight
        if (sameWidthEnabled) {
            textMaxWidth = paint.measureText(formatItem(0)).toInt()
        } else if (!TextUtils.isEmpty(maxWidthText)) {
            textMaxWidth = paint.measureText(maxWidthText).toInt()
        } else {
            // 未指定最宽的文本，须遍历测量查找最宽的作为基准
            val itemCount = getItemCount()
            for (i in 0 until itemCount) {
                val width = paint.measureText(formatItem(i)).toInt()
                textMaxWidth = Math.max(textMaxWidth, width)
            }
        }
        val metrics = paint.fontMetrics
        textMaxHeight = (metrics.bottom - metrics.top).toInt()
    }

    fun getItemCount(): Int {
        return data.size
    }

    fun <T> getItem(position: Int): T? {
        val size = data.size
        if (size == 0) {
            return null
        }
        val index = (position + size) % size
        return if (index >= 0 && index <= size - 1) {
            data[index] as T
        } else null
    }

    fun getPosition(item: Any?): Int {
        return if (item == null) {
            0
        } else data.indexOf(item)
    }

    fun <T> getCurrentItem(): T? {
        return getItem(currentPosition)
    }

    fun getVisibleItemCount(): Int {
        return visibleItemCount
    }

    fun setVisibleItemCount(@IntRange(from = 2) count: Int) {
        visibleItemCount = count
        updateVisibleItemCount()
        requestLayout()
    }

    fun isCyclicEnabled(): Boolean {
        return cyclicEnabled
    }

    fun setCyclicEnabled(isCyclic: Boolean) {
        cyclicEnabled = isCyclic
        computeFlingLimitYCoordinate()
        invalidate()
    }

    fun setOnWheelChangedListener(listener: OnWheelChangedListener?) {
        onWheelChangedListener = listener
    }

    fun setFormatter(formatter: WheelFormatter?) {
        this.formatter = formatter
    }

    fun getData(): MutableList<Any> {
        return data
    }

    fun setData(newData: MutableList<Any>?) {
        setData(newData, 0)
    }

    fun setData(newData: MutableList<Any>?, defaultValue: Any?) {
        setData(newData, findPosition(defaultValue))
    }

    fun setData(newData: MutableList<Any>?, defaultPosition: Int) {
        var newData = newData
        if (newData == null) {
            newData = mutableListOf()
        }
        data = newData
        notifyDataSetChanged(defaultPosition)
    }

    fun setDefaultValue(value: Any?) {
        setDefaultPosition(findPosition(value))
    }

    fun setDefaultPosition(position: Int) {
        notifyDataSetChanged(position)
    }

    private fun findPosition(value: Any?): Int {
        if (value == null) {
            return 0
        }
        var found = false
        var position = 0
        for (item in data) {
            if (item == null) {
                continue
            }
            if (item == value) {
                found = true
                break
            }
            if (formatter != null && formatter!!.formatItem(item) == formatter!!.formatItem(value)) {
                found = true
                break
            }
            if (item is TextProvider) {
                val text = item.provideText()
                if (text == value.toString()) {
                    found = true
                    break
                }
            }
            if (item.toString() == value.toString()) {
                found = true
                break
            }
            position++
        }
        if (!found) {
            position = 0
        }
        return position
    }

    fun isSameWidthEnabled(): Boolean {
        return sameWidthEnabled
    }

    fun setSameWidthEnabled(sameWidthEnabled: Boolean) {
        this.sameWidthEnabled = sameWidthEnabled
        computeTextWidthAndHeight()
        requestLayout()
        invalidate()
    }

    fun getMaxWidthText(): String? {
        return maxWidthText
    }

    fun setMaxWidthText(text: String?) {
        if (null == text) {
            throw NullPointerException("Maximum width text can not be null!")
        }
        maxWidthText = text
        computeTextWidthAndHeight()
        requestLayout()
        invalidate()
    }

    @ColorInt
    fun getTextColor(): Int {
        return textColor
    }

    fun setTextColor(@ColorInt color: Int) {
        textColor = color
        invalidate()
    }

    @ColorInt
    fun getSelectedTextColor(): Int {
        return selectedTextColor
    }

    fun setSelectedTextColor(@ColorInt color: Int) {
        selectedTextColor = color
        computeCurrentItemRect()
        invalidate()
    }

    @Px
    fun getTextSize(): Float {
        return textSize
    }

    fun setTextSize(@Px size: Float) {
        textSize = size
        computeTextWidthAndHeight()
        requestLayout()
        invalidate()
    }

    @Px
    fun getSelectedTextSize(): Float {
        return selectedTextSize
    }

    fun setSelectedTextSize(@Px size: Float) {
        selectedTextSize = size
        computeTextWidthAndHeight()
        requestLayout()
        invalidate()
    }

    fun getSelectedTextBold(): Boolean {
        return selectedTextBold
    }

    fun setSelectedTextBold(bold: Boolean) {
        selectedTextBold = bold
        computeTextWidthAndHeight()
        requestLayout()
        invalidate()
    }

    @Px
    fun getItemSpace(): Int {
        return itemSpace
    }

    fun setItemSpace(@Px space: Int) {
        itemSpace = space
        requestLayout()
        invalidate()
    }

    fun isIndicatorEnabled(): Boolean {
        return indicatorEnabled
    }

    fun setIndicatorEnabled(indicatorEnabled: Boolean) {
        this.indicatorEnabled = indicatorEnabled
        computeIndicatorRect()
        invalidate()
    }

    @Px
    fun getIndicatorSize(): Float {
        return indicatorSize
    }

    fun setIndicatorSize(@Px size: Float) {
        indicatorSize = size
        computeIndicatorRect()
        invalidate()
    }

    @ColorInt
    fun getIndicatorColor(): Int {
        return indicatorColor
    }

    fun setIndicatorColor(@ColorInt color: Int) {
        indicatorColor = color
        invalidate()
    }

    @Px
    fun getCurvedIndicatorSpace(): Int {
        return curvedIndicatorSpace
    }

    fun setCurvedIndicatorSpace(@Px space: Int) {
        curvedIndicatorSpace = space
        computeIndicatorRect()
        invalidate()
    }

    fun isCurtainEnabled(): Boolean {
        return curtainEnabled
    }

    fun setCurtainEnabled(curtainEnabled: Boolean) {
        this.curtainEnabled = curtainEnabled
        if (curtainEnabled) {
            indicatorEnabled = false
        }
        computeCurrentItemRect()
        invalidate()
    }

    @ColorInt
    fun getCurtainColor(): Int {
        return curtainColor
    }

    fun setCurtainColor(@ColorInt color: Int) {
        curtainColor = color
        invalidate()
    }

    @CurtainCorner
    fun getCurtainCorner(): Int {
        return curtainCorner
    }

    fun setCurtainCorner(@CurtainCorner curtainCorner: Int) {
        this.curtainCorner = curtainCorner
        invalidate()
    }

    @Px
    fun getCurtainRadius(): Float {
        return curtainRadius
    }

    fun setCurtainRadius(@Px curtainRadius: Float) {
        this.curtainRadius = curtainRadius
        invalidate()
    }

    fun isAtmosphericEnabled(): Boolean {
        return atmosphericEnabled
    }

    fun setAtmosphericEnabled(atmosphericEnabled: Boolean) {
        this.atmosphericEnabled = atmosphericEnabled
        invalidate()
    }

    fun isCurvedEnabled(): Boolean {
        return curvedEnabled
    }

    fun setCurvedEnabled(isCurved: Boolean) {
        curvedEnabled = isCurved
        requestLayout()
        invalidate()
    }

    fun getCurvedMaxAngle(): Int {
        return curvedMaxAngle
    }

    fun setCurvedMaxAngle(curvedMaxAngle: Int) {
        this.curvedMaxAngle = curvedMaxAngle
        requestLayout()
        invalidate()
    }

    @ItemTextAlign
    fun getTextAlign(): Int {
        return textAlign
    }

    fun setTextAlign(@ItemTextAlign align: Int) {
        textAlign = align
        updatePaintTextAlign()
        computeDrawnCenterCoordinate()
        invalidate()
    }

    private fun updatePaintTextAlign() {
        when (textAlign) {
            ItemTextAlign.LEFT -> paint.textAlign = Paint.Align.LEFT
            ItemTextAlign.RIGHT -> paint.textAlign = Paint.Align.RIGHT
            ItemTextAlign.CENTER -> paint.textAlign = Paint.Align.CENTER
            else -> paint.textAlign = Paint.Align.CENTER
        }
    }

    fun getTypeface(): Typeface {
        return paint.typeface
    }

    fun setTypeface(typeface: Typeface?) {
        if (typeface == null) {
            return
        }
        paint.typeface = typeface
        computeTextWidthAndHeight()
        requestLayout()
        invalidate()
    }

    private fun notifyDataSetChanged(position: Int) {
        var position = position
        position = Math.min(position, getItemCount() - 1)
        position = Math.max(position, 0)
        scrollOffsetYCoordinate = 0
        defaultItem = getItem<Any>(position)
        defaultItemPosition = position
        currentPosition = position
        updatePaintTextAlign()
        //当设置了选中项文字加大加粗，此处重新计算文字宽高的话滑动会导致其他条目错位
        //computeTextWidthAndHeight();
        computeFlingLimitYCoordinate()
        computeIndicatorRect()
        computeCurrentItemRect()
        requestLayout()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val modeWidth = MeasureSpec.getMode(widthMeasureSpec)
        val modeHeight = MeasureSpec.getMode(heightMeasureSpec)
        val sizeWidth = MeasureSpec.getSize(widthMeasureSpec)
        val sizeHeight = MeasureSpec.getSize(heightMeasureSpec)
        // Correct sizes of original content
        var resultWidth = textMaxWidth
        var resultHeight = textMaxHeight * visibleItemCount + itemSpace * (visibleItemCount - 1)
        // Correct view sizes again if curved is enable
        if (curvedEnabled) {
            resultHeight = (2 * resultHeight / Math.PI).toInt()
        }
        // Consideration padding influence the view sizes
        resultWidth += paddingLeft + paddingRight
        resultHeight += paddingTop + paddingBottom
        // Consideration sizes of parent can influence the view sizes
        resultWidth = measureSize(modeWidth, sizeWidth, resultWidth)
        resultHeight = measureSize(modeHeight, sizeHeight, resultHeight)
        setMeasuredDimension(resultWidth, resultHeight)
    }

    private fun measureSize(mode: Int, sizeExpect: Int, sizeActual: Int): Int {
        var realSize: Int
        if (mode == MeasureSpec.EXACTLY) {
            realSize = sizeExpect
        } else {
            realSize = sizeActual
            if (mode == MeasureSpec.AT_MOST) {
                realSize = Math.min(realSize, sizeExpect)
            }
        }
        return realSize
    }

    override fun onSizeChanged(w: Int, h: Int, ow: Int, oh: Int) {
        // Set content region
        rectDrawn[paddingLeft, paddingTop, width - paddingRight] = height - paddingBottom
        // Get the center coordinates of content region
        wheelCenterXCoordinate = rectDrawn.centerX()
        wheelCenterYCoordinate = rectDrawn.centerY()
        // Correct item drawn center
        computeDrawnCenterCoordinate()
        halfWheelHeight = rectDrawn.height() / 2
        itemHeight = rectDrawn.height() / visibleItemCount
        halfItemHeight = itemHeight / 2
        // Initialize fling max Y-coordinates
        computeFlingLimitYCoordinate()
        // Correct region of indicator
        computeIndicatorRect()
        // Correct region of current select item
        computeCurrentItemRect()
    }

    private fun computeDrawnCenterCoordinate() {
        drawnCenterXCoordinate = when (textAlign) {
            ItemTextAlign.LEFT -> rectDrawn.left
            ItemTextAlign.RIGHT -> rectDrawn.right
            ItemTextAlign.CENTER -> wheelCenterXCoordinate
            else -> wheelCenterXCoordinate
        }
        drawnCenterYCoordinate = (wheelCenterYCoordinate - (paint.ascent() + paint.descent()) / 2).toInt()
    }

    private fun computeFlingLimitYCoordinate() {
        val currentItemOffset = defaultItemPosition * itemHeight
        minFlingYCoordinate = if (cyclicEnabled) Int.MIN_VALUE else -itemHeight * (getItemCount() - 1) + currentItemOffset
        maxFlingYCoordinate = if (cyclicEnabled) Int.MAX_VALUE else currentItemOffset
    }

    private fun computeIndicatorRect() {
        if (!indicatorEnabled) {
            return
        }
        val indicatorSpace = if (curvedEnabled) curvedIndicatorSpace else 0
        val halfIndicatorSize = (indicatorSize / 2f).toInt()
        val indicatorHeadCenterYCoordinate = wheelCenterYCoordinate + halfItemHeight + indicatorSpace
        val indicatorFootCenterYCoordinate = wheelCenterYCoordinate - halfItemHeight - indicatorSpace
        rectIndicatorHead[rectDrawn.left, indicatorHeadCenterYCoordinate - halfIndicatorSize, rectDrawn.right] =
            indicatorHeadCenterYCoordinate + halfIndicatorSize
        rectIndicatorFoot[rectDrawn.left, indicatorFootCenterYCoordinate - halfIndicatorSize, rectDrawn.right] =
            indicatorFootCenterYCoordinate + halfIndicatorSize
    }

    private fun computeCurrentItemRect() {
        if (!curtainEnabled && selectedTextColor == 0 /* Color.TRANSPARENT */) {
            return
        }
        rectCurrentItem[rectDrawn.left, wheelCenterYCoordinate - halfItemHeight, rectDrawn.right] = wheelCenterYCoordinate + halfItemHeight
    }

    override fun onDraw(canvas: Canvas) {
        if (null != onWheelChangedListener) {
            onWheelChangedListener!!.onWheelScrolled(this, scrollOffsetYCoordinate)
        }
        if (itemHeight - halfDrawnItemCount <= 0) {
            return
        }
        drawCurtain(canvas)
        drawIndicator(canvas)
        drawAllItem(canvas)
    }

    private fun drawAllItem(canvas: Canvas) {
        val drawnDataStartPos = -1 * scrollOffsetYCoordinate / itemHeight - halfDrawnItemCount
        var drawnDataPosition = drawnDataStartPos + defaultItemPosition
        var drawnOffsetPos = -1 * halfDrawnItemCount
        while (drawnDataPosition < drawnDataStartPos + defaultItemPosition + drawnItemCount) {
            initTextPaint()
            val isCenterItem = drawnDataPosition == drawnDataStartPos + defaultItemPosition + drawnItemCount / 2
            val drawnItemCenterYCoordinate = drawnCenterYCoordinate + drawnOffsetPos * itemHeight + scrollOffsetYCoordinate % itemHeight
            val centerYCoordinateAbs = Math.abs(drawnCenterYCoordinate - drawnItemCenterYCoordinate)
            // Correct ratio of item's drawn center to wheel center
            val ratio = (drawnCenterYCoordinate - centerYCoordinateAbs - rectDrawn.top) * 1f /
                    (drawnCenterYCoordinate - rectDrawn.top)
            val degree = computeDegree(drawnItemCenterYCoordinate, ratio)
            val distanceToCenter = computeYCoordinateAtAngle(degree)
            if (curvedEnabled) {
                var transXCoordinate = wheelCenterXCoordinate
                when (textAlign) {
                    ItemTextAlign.LEFT -> transXCoordinate = rectDrawn.left
                    ItemTextAlign.RIGHT -> transXCoordinate = rectDrawn.right
                    ItemTextAlign.CENTER -> {}
                    else -> {}
                }
                val transYCoordinate = wheelCenterYCoordinate - distanceToCenter
                camera.save()
                camera.rotateX(degree)
                camera.getMatrix(matrixRotate)
                camera.restore()
                matrixRotate.preTranslate(-transXCoordinate.toFloat(), -transYCoordinate)
                matrixRotate.postTranslate(transXCoordinate.toFloat(), transYCoordinate)
                camera.save()
                camera.translate(0f, 0f, computeDepth(degree).toFloat())
                camera.getMatrix(matrixDepth)
                camera.restore()
                matrixDepth.preTranslate(-transXCoordinate.toFloat(), -transYCoordinate)
                matrixDepth.postTranslate(transXCoordinate.toFloat(), transYCoordinate)
                matrixRotate.postConcat(matrixDepth)
            }
            computeAndSetAtmospheric(centerYCoordinateAbs)
            // Correct item's drawn center Y coordinate base on curved state
            val drawCenterYCoordinate = if (curvedEnabled) drawnCenterYCoordinate - distanceToCenter else drawnItemCenterYCoordinate.toFloat()
            drawItemRect(canvas, drawnDataPosition, isCenterItem, drawCenterYCoordinate)
            drawnDataPosition++
            drawnOffsetPos++
        }
    }

    private fun drawItemRect(canvas: Canvas, dataPosition: Int, isCenterItem: Boolean, drawCenterYCoordinate: Float) {
        // Judges need to draw different color for current item or not
        if (selectedTextColor == 0 /* Color.TRANSPARENT */) {
            //没有设置选中项颜色，绘制所有项
            canvas.save()
            canvas.clipRect(rectDrawn)
            if (curvedEnabled) {
                canvas.concat(matrixRotate)
            }
            drawItemText(canvas, dataPosition, drawCenterYCoordinate)
            canvas.restore()
            return
        }
        if (textSize == selectedTextSize && !selectedTextBold) {
            //没有设置选中项的加大加粗，绘制所有项
            canvas.save()
            if (curvedEnabled) {
                canvas.concat(matrixRotate)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                canvas.clipOutRect(rectCurrentItem)
            } else {
                canvas.clipRect(rectCurrentItem, Region.Op.DIFFERENCE)
            }
            drawItemText(canvas, dataPosition, drawCenterYCoordinate)
            canvas.restore()
            paint.color = selectedTextColor
            canvas.save()
            if (curvedEnabled) {
                canvas.concat(matrixRotate)
            }
            canvas.clipRect(rectCurrentItem)
            drawItemText(canvas, dataPosition, drawCenterYCoordinate)
            canvas.restore()
            return
        }
        if (!isCenterItem) {
            // 绘制非选中项
            canvas.save()
            if (curvedEnabled) {
                canvas.concat(matrixRotate)
            }
            drawItemText(canvas, dataPosition, drawCenterYCoordinate)
            canvas.restore()
            return
        }

        // 绘制选中项
        paint.color = selectedTextColor
        paint.textSize = selectedTextSize
        paint.isFakeBoldText = selectedTextBold
        canvas.save()
        if (curvedEnabled) {
            canvas.concat(matrixRotate)
        }
        drawItemText(canvas, dataPosition, drawCenterYCoordinate)
        canvas.restore()
    }

    private fun drawItemText(canvas: Canvas, dataPosition: Int, drawCenterYCoordinate: Float) {
        var hasCut = false
        val ellipsis = "..."
        val measuredWidth = measuredWidth
        val ellipsisWidth = paint.measureText(ellipsis)
        var data = obtainItemText(dataPosition)
        while (paint.measureText(data) + ellipsisWidth - measuredWidth > 0) {
            // 超出控件宽度则省略部分文字
            val length = data!!.length
            if (length > 1) {
                data = data.substring(0, length - 1)
                hasCut = true
            }
        }
        if (hasCut) {
            data = data + ellipsis
        }
        canvas.drawText(data!!, drawnCenterXCoordinate.toFloat(), drawCenterYCoordinate, paint)
    }

    private fun computeDegree(drawnItemCenterYCoordinate: Int, ratio: Float): Float {
        // Correct unit
        var unit = 0
        if (drawnItemCenterYCoordinate > drawnCenterYCoordinate) {
            unit = 1
        } else if (drawnItemCenterYCoordinate < drawnCenterYCoordinate) {
            unit = -1
        }
        return clamp(-(1 - ratio) * curvedMaxAngle * unit, -curvedMaxAngle.toFloat(), curvedMaxAngle.toFloat())
    }

    private fun clamp(value: Float, min: Float, max: Float): Float {
        return if (value < min) {
            min
        } else Math.min(value, max)
    }

    private fun obtainItemText(drawnDataPosition: Int): String? {
        var data: String? = ""
        val itemCount = getItemCount()
        if (cyclicEnabled) {
            if (itemCount != 0) {
                var actualPosition = drawnDataPosition % itemCount
                actualPosition = if (actualPosition < 0) actualPosition + itemCount else actualPosition
                data = formatItem(actualPosition)
            }
        } else {
            if (isPositionInRange(drawnDataPosition, itemCount)) {
                data = formatItem(drawnDataPosition)
            }
        }
        return data
    }

    fun formatItem(position: Int): String? {
        return formatItem(getItem<Any>(position))
    }

    fun formatItem(item: Any?): String? {
        if (item == null) {
            return ""
        }
        if (item is TextProvider) {
            return item.provideText()
        }
        return if (formatter != null) {
            formatter!!.formatItem(item)
        } else item.toString()
    }

    private fun computeAndSetAtmospheric(abs: Int) {
        if (atmosphericEnabled) {
            var alpha = ((drawnCenterYCoordinate - abs) * 1.0f / drawnCenterYCoordinate * 255).toInt()
            alpha = Math.max(alpha, 0)
            paint.alpha = alpha
        }
    }

    private fun drawCurtain(canvas: Canvas) {
        // Need to draw curtain or not
        if (!curtainEnabled) {
            return
        }
        val red = Color.red(curtainColor)
        val green = Color.green(curtainColor)
        val blue = Color.blue(curtainColor)
        paint.color = Color.argb(128, red, green, blue)
        paint.style = Paint.Style.FILL
        if (curtainRadius > 0) {
            val path = Path()
            val radii: FloatArray
            radii = when (curtainCorner) {
                CurtainCorner.ALL -> floatArrayOf(
                    curtainRadius, curtainRadius, curtainRadius, curtainRadius,
                    curtainRadius, curtainRadius, curtainRadius, curtainRadius
                )
                CurtainCorner.TOP -> floatArrayOf(
                    curtainRadius, curtainRadius, curtainRadius, curtainRadius, 0f, 0f, 0f, 0f
                )
                CurtainCorner.BOTTOM -> floatArrayOf(
                    0f, 0f, 0f, 0f, curtainRadius, curtainRadius, curtainRadius, curtainRadius
                )
                CurtainCorner.LEFT -> floatArrayOf(
                    curtainRadius, curtainRadius, 0f, 0f, 0f, 0f, curtainRadius, curtainRadius
                )
                CurtainCorner.RIGHT -> floatArrayOf(
                    0f, 0f, curtainRadius, curtainRadius, curtainRadius, curtainRadius, 0f, 0f
                )
                else -> floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
            }
            path.addRoundRect(RectF(rectCurrentItem), radii, Path.Direction.CCW)
            canvas.drawPath(path, paint)
            return
        }
        canvas.drawRect(rectCurrentItem, paint)
    }

    private fun drawIndicator(canvas: Canvas) {
        // Need to draw indicator or not
        if (!indicatorEnabled) {
            return
        }
        paint.color = indicatorColor
        paint.style = Paint.Style.FILL
        canvas.drawRect(rectIndicatorHead, paint)
        canvas.drawRect(rectIndicatorFoot, paint)
    }

    private fun isPositionInRange(position: Int, itemCount: Int): Boolean {
        return position >= 0 && position < itemCount
    }

    private fun computeYCoordinateAtAngle(degree: Float): Float {
        // Compute y-coordinate for item at degree.
        return sinDegree(degree) / sinDegree(curvedMaxAngle.toFloat()) * halfWheelHeight
    }

    private fun sinDegree(degree: Float): Float {
        return Math.sin(Math.toRadians(degree.toDouble())).toFloat()
    }

    private fun computeDepth(degree: Float): Int {
        return (halfWheelHeight - Math.cos(Math.toRadians(degree.toDouble())) * halfWheelHeight).toInt()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isEnabled) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> handleActionDown(event)
                MotionEvent.ACTION_MOVE -> handleActionMove(event)
                MotionEvent.ACTION_UP -> handleActionUp(event)
                MotionEvent.ACTION_CANCEL -> handleActionCancel(event)
                else -> {}
            }
        }
        if (isClick) {
            //onTouchEvent should call performClick when a click is detected
            performClick()
        }
        return true
    }

    private fun handleActionDown(event: MotionEvent) {
        if (null != parent) {
            parent.requestDisallowInterceptTouchEvent(true)
        }
        obtainOrClearTracker()
        tracker!!.addMovement(event)
        if (!scroller.isFinished) {
            scroller.abortAnimation()
            isForceFinishScroll = true
        }
        lastPointYCoordinate = event.y.toInt()
        downPointYCoordinate = lastPointYCoordinate
    }

    private fun handleActionMove(event: MotionEvent) {
        val endPoint = computeDistanceToEndPoint(scroller.finalY % itemHeight)
        if (Math.abs(downPointYCoordinate - event.y) < touchSlop && endPoint > 0) {
            isClick = true
            return
        }
        isClick = false
        if (null != tracker) {
            tracker!!.addMovement(event)
        }
        if (null != onWheelChangedListener) {
            onWheelChangedListener!!.onWheelScrollStateChanged(this, ScrollState.DRAGGING)
        }
        // Scroll WheelPicker's content
        val move = event.y - lastPointYCoordinate
        if (Math.abs(move) < 1) {
            return
        }
        scrollOffsetYCoordinate += move.toInt()
        lastPointYCoordinate = event.y.toInt()
        invalidate()
    }

    private fun handleActionUp(event: MotionEvent) {
        if (null != parent) {
            parent.requestDisallowInterceptTouchEvent(false)
        }
        if (isClick) {
            return
        }
        var yVelocity = 0
        if (null != tracker) {
            tracker!!.addMovement(event)
            tracker!!.computeCurrentVelocity(1000, maximumVelocity.toFloat())
            yVelocity = tracker!!.yVelocity.toInt()
        }

        // Judge scroll or fling base on current velocity
        isForceFinishScroll = false
        if (Math.abs(yVelocity) > minimumVelocity) {
            scroller.fling(
                0, scrollOffsetYCoordinate, 0, yVelocity, 0,
                0, minFlingYCoordinate, maxFlingYCoordinate
            )
            val endPoint = computeDistanceToEndPoint(scroller.finalY % itemHeight)
            scroller.finalY = scroller.finalY + endPoint
        } else {
            val endPoint = computeDistanceToEndPoint(scrollOffsetYCoordinate % itemHeight)
            scroller.startScroll(0, scrollOffsetYCoordinate, 0, endPoint)
        }
        // Correct coordinates
        if (!cyclicEnabled) {
            if (scroller.finalY > maxFlingYCoordinate) {
                scroller.finalY = maxFlingYCoordinate
            } else if (scroller.finalY < minFlingYCoordinate) {
                scroller.finalY = minFlingYCoordinate
            }
        }
        handler.post(this)
        cancelTracker()
    }

    private fun handleActionCancel(event: MotionEvent) {
        if (null != parent) {
            parent.requestDisallowInterceptTouchEvent(false)
        }
        cancelTracker()
    }

    private fun obtainOrClearTracker() {
        if (null == tracker) {
            tracker = VelocityTracker.obtain()
        } else {
            tracker!!.clear()
        }
    }

    private fun cancelTracker() {
        if (null != tracker) {
            tracker!!.recycle()
            tracker = null
        }
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    private fun computeDistanceToEndPoint(remainder: Int): Int {
        return if (Math.abs(remainder) > halfItemHeight) {
            if (scrollOffsetYCoordinate < 0) {
                -itemHeight - remainder
            } else {
                itemHeight - remainder
            }
        } else {
            -1 * remainder
        }
    }

    override fun run() {
        if (itemHeight == 0) {
            return
        }
        val itemCount = getItemCount()
        if (itemCount == 0) {
            if (null != onWheelChangedListener) {
                onWheelChangedListener!!.onWheelScrollStateChanged(this, ScrollState.IDLE)
            }
            return
        }
        if (scroller.isFinished && !isForceFinishScroll) {
            var position = computePosition(itemCount)
            position = if (position < 0) position + itemCount else position
            currentPosition = position
            if (null != onWheelChangedListener) {
                onWheelChangedListener!!.onWheelSelected(this, position)
                onWheelChangedListener!!.onWheelScrollStateChanged(this, ScrollState.IDLE)
            }
            postInvalidate()
            return
        }
        // Scroll not finished
        if (scroller.computeScrollOffset()) {
            if (null != onWheelChangedListener) {
                onWheelChangedListener!!.onWheelScrollStateChanged(this, ScrollState.SCROLLING)
            }
            scrollOffsetYCoordinate = scroller.currY
            val position = computePosition(itemCount)
            if (lastScrollPosition != position) {
                if (position == 0 && lastScrollPosition == itemCount - 1) {
                    if (null != onWheelChangedListener) {
                        onWheelChangedListener!!.onWheelLoopFinished(this)
                    }
                }
                lastScrollPosition = position
            }
            postInvalidate()
            handler.postDelayed(this, 20)
        }
    }

    private fun computePosition(itemCount: Int): Int {
        return (-1 * scrollOffsetYCoordinate / itemHeight + defaultItemPosition) % itemCount
    }

    fun scrollTo(position: Int) {
        post { notifyDataSetChanged(position) }
    }

    fun smoothScrollTo(position: Int) {
        if (isInEditMode) {
            scrollTo(position)
            return
        }
        val differencesLines = currentPosition - position
        val newScrollOffsetYCoordinate = scrollOffsetYCoordinate + differencesLines * itemHeight
        val animator = ValueAnimator.ofInt(scrollOffsetYCoordinate, newScrollOffsetYCoordinate)
        animator.duration = 300
        animator.addUpdateListener { animation ->
            scrollOffsetYCoordinate = animation.animatedValue as Int
            invalidate()
        }
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                scrollTo(position)
            }
        })
        animator.start()
    }

    companion object {
        @Deprecated("")
        val SCROLL_STATE_IDLE = ScrollState.IDLE

        @Deprecated("")
        val SCROLL_STATE_DRAGGING = ScrollState.DRAGGING

        @Deprecated("")
        val SCROLL_STATE_SCROLLING = ScrollState.SCROLLING
    }
}
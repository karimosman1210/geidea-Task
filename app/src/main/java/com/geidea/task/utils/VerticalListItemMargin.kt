package com.geidea.task.utils

import android.graphics.Rect
import android.view.View
import androidx.annotation.IntRange
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/* recycler view item decoration to add margin from all side */
class VerticalListItemMargin(
    @IntRange(from = 1) private val numOfColumns: Int = 1,
    @IntRange(from = 0) private val top: Int = 0,
    @IntRange(from = 0) private val bottom: Int = 0,
    @IntRange(from = 0) private val left: Int = 0,
    @IntRange(from = 0) private val right: Int = 0,
    private val isRTL: Boolean = false,
) :
    ItemDecoration() {

    constructor(
        @IntRange(from = 1) numOfColumns: Int = 1,
        @IntRange(from = 0) margin: Int = 0,
        isRTL: Boolean = false,
    ) : this(numOfColumns, margin, margin, margin, margin, isRTL)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val itemPosition = parent.getChildLayoutPosition(view)

        // add bottom margin to all items
        outRect.bottom = bottom

        // only add top margin to the items in the first row
        if (itemPosition < numOfColumns) {
            outRect.top = top
        }

        // check for number of Columns
        if (numOfColumns == 1) { // one column
            outRect.left = left
            outRect.right = right
        } else { // more than one columns
            // get the Column Index
            val columnIndex: Int = (view.layoutParams as GridLayoutManager.LayoutParams).spanIndex

            if (isRTL) { // ex: Arabic Language
                when (columnIndex) {
                    0 -> { // first column
                        outRect.right = right
                        outRect.left = left / 2
                    }
                    (numOfColumns - 1) -> { // last column
                        outRect.left = left
                        outRect.right = right / 2
                    }
                    else -> { // all other columns
                        outRect.left = left / 2
                        outRect.right = right / 2
                    }
                }
            } else { // ex: English Language
                when (columnIndex) {
                    0 -> { // first column
                        outRect.left = left
                        outRect.right = right / 2
                    }
                    (numOfColumns - 1) -> { // last column
                        outRect.right = right
                        outRect.left = left / 2
                    }
                    else -> { // all other columns
                        outRect.left = left / 2
                        outRect.right = right / 2
                    }
                }
            }
        }

    }
}
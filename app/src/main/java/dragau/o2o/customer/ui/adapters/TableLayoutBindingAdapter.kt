package dragau.o2o.customer.ui.adapters

import android.util.SparseBooleanArray
import android.widget.TableLayout
import androidx.databinding.BindingAdapter

import java.util.regex.Pattern

object TableLayoutBindingAdapter {

    private val sColumnPattern = Pattern.compile("\\s*,\\s*")

    private val MAX_COLUMNS = 20

    @BindingAdapter("android:collapseColumns")
    fun setCollapseColumns(view: TableLayout, columnsStr: CharSequence) {
        val columns = parseColumns(columnsStr)
        for (i in 0 until MAX_COLUMNS) {
            val isCollapsed = columns.get(i, false)
            if (isCollapsed != view.isColumnCollapsed(i)) {
                view.setColumnCollapsed(i, isCollapsed)
            }
        }
    }

    @BindingAdapter("android:shrinkColumns")
    fun setShrinkColumns(view: TableLayout, columnsStr: CharSequence?) {
        if (columnsStr != null && columnsStr.length > 0 && columnsStr[0] == '*') {
            view.isShrinkAllColumns = true
        } else {
            view.isShrinkAllColumns = false
            val columns = parseColumns(columnsStr)
            val columnCount = columns.size()
            for (i in 0 until columnCount) {
                val column = columns.keyAt(i)
                val shrinkable = columns.valueAt(i)
                if (shrinkable) {
                    view.setColumnShrinkable(column, shrinkable)
                }
            }
        }
    }

    @BindingAdapter("android:stretchColumns")
    fun setStretchColumns(view: TableLayout, columnsStr: CharSequence?) {
        if (columnsStr != null && columnsStr.length > 0 && columnsStr[0] == '*') {
            view.isStretchAllColumns = true
        } else {
            view.isStretchAllColumns = false
            val columns = parseColumns(columnsStr)
            val columnCount = columns.size()
            for (i in 0 until columnCount) {
                val column = columns.keyAt(i)
                val stretchable = columns.valueAt(i)
                if (stretchable) {
                    view.setColumnStretchable(column, stretchable)
                }
            }
        }
    }

    private fun parseColumns(sequence: CharSequence?): SparseBooleanArray {
        val columns = SparseBooleanArray()
        if (sequence == null) {
            return columns
        }
        val columnDefs = sColumnPattern.split(sequence)

        for (columnIdentifier in columnDefs) {
            try {
                val columnIndex = Integer.parseInt(columnIdentifier)
                // only valid, i.e. positive, columns indexes are handled
                if (columnIndex >= 0) {
                    // putting true in this sparse array indicates that the
                    // column index was defined in the XML file
                    columns.put(columnIndex, true)
                }
            } catch (e: NumberFormatException) {
                // we just ignore columns that don't exist
            }

        }

        return columns
    }
}
package ch.frankel.filerenamer.gui

import javax.swing.{ DefaultCellEditor, JCheckBox, JTable }
import javax.swing.event.CellEditorListener

import scala.swing.Publisher

import ch.frankel.filerenamer.Constant.INDEX_PROP

class CheckboxTableCellEditor(val listener: CellEditorListener) extends DefaultCellEditor(new JCheckBox) with Publisher {

  addCellEditorListener(listener)

  override def getTableCellEditorComponent(table: JTable, value: Any, isSelected: Boolean, row: Int, column: Int) = {

    val tableCellEditorComponent = super.getTableCellEditorComponent(table, value, isSelected, row, column)

    tableCellEditorComponent.asInstanceOf[JCheckBox].putClientProperty(INDEX_PROP, row)

    tableCellEditorComponent
  }
}
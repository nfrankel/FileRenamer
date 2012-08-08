package ch.frankel.filerenamer.gui

import javax.swing.BorderFactory
import scala.swing.Table
import ch.frankel.filerenamer.model.FileTableModel
import javax.swing.JTable

class FileRenamerTable(override val model: FileTableModel, val listener: CheckboxTableCellEditorListener) extends Table {

  showGrid = true

  peer.setModel(model)

  // Set header titles
  peer.getTableHeader.getColumnModel.getColumn(0).setHeaderValue("")
  peer.getTableHeader.getColumnModel.getColumn(1).setHeaderValue("Original")
  peer.getTableHeader.getColumnModel.getColumn(2).setHeaderValue("Final")

  
  peer.getColumnModel.getColumn(0).setPreferredWidth(30)
  peer.getColumnModel.getColumn(0).setMaxWidth(30)

  override protected def editor(row: Int, column: Int) = {

    if (column == 0) new CheckboxTableCellEditor(listener)

    else super.editor(row, column)
  }
}
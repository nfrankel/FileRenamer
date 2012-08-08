package ch.frankel.filerenamer.model

import java.io.File

import javax.swing.table.AbstractTableModel

import scala.collection.mutable.ListBuffer

class FileTableModel extends AbstractTableModel {

  val files = new ListBuffer[RenameFile]

  def getRowCount = files.size

  def getColumnCount = 3

  def getValueAt(rowIndex: Int, columnIndex: Int): Object = {

    columnIndex match {

      case 0 => boolean2Boolean(files(rowIndex).checked)
      case 1 => files(rowIndex).initialFile.getName
      case 2 => files(rowIndex).targetName
    }
  }

  def set(array: Array[File]) = {

    array.foreach(f => files += new RenameFile(f))

    fireTableDataChanged
  }

  def clear = files.clear

  override def isCellEditable(row: Int, col: Int) = col == 0

  override def setValueAt(value: Any, rowIndex: Int, columnIndex: Int) = {

    columnIndex match {

      case 0 => files(rowIndex).checked = value.asInstanceOf[Boolean]
    }
  }

  def updatePreview(pattern: String, target: String) = {

    files.foreach(f => f.updatePreview(pattern, target))

    fireTableDataChanged()
  }

  def updatePreview(row: Int, pattern: String, target: String) = {

    files(row).updatePreview(pattern, target)

    fireTableRowsUpdated(row, row)
  }

  def inverseSelectFiles(pattern: String, target: String) = {

    files.foreach(f => {

      f.inverse
      f.updatePreview(pattern, target)
    })

    fireTableDataChanged()
  }

  def renameFiles = {

    files.foreach(f => f.rename)

    fireTableDataChanged()
  }
}
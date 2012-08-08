package ch.frankel.filerenamer.gui

import javax.swing.{DefaultCellEditor, JCheckBox}
import javax.swing.event.{CellEditorListener, ChangeEvent}
import scala.swing.Publisher
import ch.frankel.filerenamer.event.CheckEvent
import ch.frankel.filerenamer.Constant

class CheckboxTableCellEditorListener extends CellEditorListener with Publisher {

  def editingStopped(e: ChangeEvent) {

    val index = e.getSource.asInstanceOf[DefaultCellEditor].getComponent.asInstanceOf[JCheckBox].getClientProperty(Constant.INDEX_PROP).asInstanceOf[Int]

    publish(new CheckEvent(index))
  }

  def editingCanceled(e: ChangeEvent) = {}
}
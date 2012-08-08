package ch.frankel.filerenamer

import java.awt.Insets
import java.io.File

import javax.swing.filechooser.FileFilter

import scala.swing.{ Button, Dimension, FileChooser, Label, MainFrame, ScrollPane, SimpleSwingApplication, TextField }
import scala.swing.FileChooser.Result.Approve
import scala.swing.FileChooser.SelectionMode.DirectoriesOnly
import scala.swing.GridBagPanel
import scala.swing.GridBagPanel.{ Anchor, Fill }
import scala.swing.event.{ ButtonClicked, ValueChanged }

import ch.frankel.filerenamer.event.CheckEvent
import ch.frankel.filerenamer.gui.{ CheckboxTableCellEditorListener, FileRenamerTable }
import ch.frankel.filerenamer.model.{ FileTableModel, RenameFile }

object FileRenamerApplication extends SimpleSwingApplication {

  /** Root direcotry path field. */
  val dirPath = new TextField { preferredSize = new Dimension(250, 28) }

  val model = new FileTableModel

  val pattern = new TextField

  val target = new TextField

  override def top = new MainFrame {

    /** Choose root directory dialog open button. */
    val dirPathChooseButon = new Button("Choose")

    val applyButton = new Button("Apply")

    val inverseSelectButton = new Button("Inverse selection")

    val listener = new CheckboxTableCellEditorListener

    title = "File renamer"
    contents = new GridBagPanel {

      val constraints = new Constraints

      constraints.insets = new Insets(4, 4, 4, 4)
      constraints.anchor = Anchor.West

      val firstLine = new GridBagPanel {

        val constraints = new Constraints

        val dirPathLabel = new Label("Root folder:");

        layout(dirPathLabel) = constraints

        constraints.fill = Fill.Horizontal
        constraints.weightx = 1.0

        layout(dirPath) = constraints

        constraints.fill = Fill.None
        constraints.weightx = 0.0

        layout(dirPathChooseButon) = constraints
      }

      constraints.fill = Fill.Horizontal
      constraints.weightx = 1.0
      constraints.gridwidth = 4

      layout(firstLine) = constraints

      val patternLabel = new Label("Pattern:")

      constraints.gridy = 1
      constraints.fill = Fill.None
      constraints.weightx = 0.0
      constraints.gridwidth = 1

      layout(patternLabel) = constraints

      constraints.weightx = 0.5
      constraints.gridwidth = 1
      constraints.fill = Fill.Horizontal

      layout(pattern) = constraints

      val resultLabel = new Label("Result:")

      constraints.weightx = 0.0
      constraints.fill = Fill.None

      layout(resultLabel) = constraints

      constraints.weightx = 0.5
      constraints.fill = Fill.Horizontal

      layout(target) = constraints

      val scrollPane = new ScrollPane(new FileRenamerTable(model, listener)) { preferredSize = new Dimension(320, 240) }

      constraints.fill = Fill.Both
      constraints.gridy = 2
      constraints.gridwidth = 4
      constraints.weightx = 1.0
      constraints.weighty = 1.0

      layout(scrollPane) = constraints

      constraints.fill = Fill.None
      constraints.anchor = Anchor.West
      constraints.gridy = 3
      constraints.weighty = 0.0

      layout(inverseSelectButton) = constraints

      constraints.anchor = Anchor.East
      constraints.gridx = 2

      layout(applyButton) = constraints
    }

    listenTo(dirPath, dirPathChooseButon, applyButton, pattern, target, listener, inverseSelectButton)

    reactions += {

      case ButtonClicked(`dirPathChooseButon`) => {

        val chooser: FileChooser = new FileChooser(new File(dirPath.text))

        chooser.fileSelectionMode = DirectoriesOnly
        chooser.fileFilter = new FileFilter {

          override def accept(f: File): Boolean = f.isDirectory

          override def getDescription = "Directory"
        }

        if (chooser.showOpenDialog(null) == Approve) {

          dirPath.text = chooser.selectedFile.getCanonicalPath

          populateFiles
        }
      }

      case ButtonClicked(`applyButton`) => {

        renameFiles
        populateFiles
        previewFiles
      }

      case ButtonClicked(`inverseSelectButton`) => {

        inverseSelectFiles
      }

      case ValueChanged(`dirPath`) => {
        populateFiles
        previewFiles
      }

      case ValueChanged(`target`)  => previewFiles
      case ValueChanged(`pattern`) => previewFiles

      case e: CheckEvent           => previewFile(e.index)
    }
    
    peer.setLocationRelativeTo(null)
  }
  
  def populateFiles = {

    var dir = new File(dirPath.text)

    model.clear

    if (dir exists) {

      var filter = new java.io.FileFilter { override def accept(f: File) = f.isFile }

      model.set(dir.listFiles(filter))
    }
  }

  def previewFile(row: Int) = model.updatePreview(row, pattern.text, target.text)

  def previewFiles = model.updatePreview(pattern.text, target.text)

  def renameFiles = model.renameFiles

  def inverseSelectFiles = model.inverseSelectFiles(pattern.text, target.text)
}
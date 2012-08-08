package ch.frankel.filerenamer.model

import java.io.File

class RenameFile private (val initialFile: File, var targetName: String, var checked: Boolean) {

  def this(f: File) = this(f, f.getName, true)

  def updatePreview(pattern: String, target: String) = {

    val initialName = initialFile.getName

    targetName = checked match {

      case true  => initialName.replace(pattern, target)
      case false => initialName
    }
  }

  def rename = {

    if (checked) {

      val targetFile = new File(initialFile.getParentFile(), targetName)

      initialFile.renameTo(targetFile)
    }
  }

  def inverse = checked = !checked
}
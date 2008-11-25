using System;
using System.Collections.Generic;
using System.Text;
using System.Diagnostics;
using System.IO;
using Microsoft.Win32;
using System.Windows.Forms;
using System.Security.Permissions;

namespace concoursjeunes_applyupdate {
	//[assembly: RegistryPermissionAttribute(SecurityAction.RequestMinimum, ViewAndModify = "HKEY_CURRENT_USER")]
	class Program {
		static void Main(string[] args) {
			if (args.Length == 2) {
				RegistryKey HKLM = Registry.LocalMachine;
                RegistryKey javaReg = HKLM.OpenSubKey(@"SOFTWARE\JavaSoft\Java Runtime Environment");

                if (javaReg == null)
                    javaReg = HKLM.OpenSubKey(@"SOFTWARE\Wow6432Node\JavaSoft\Java Runtime Environment");

                if (javaReg != null)
                {
                    object version = javaReg.GetValue(@"CurrentVersion");
                    object javaPath = javaReg.OpenSubKey((string)version).GetValue("JavaHome");

					ProcessStartInfo startInfo = new ProcessStartInfo(javaPath + @"\bin\javaw.exe");
					startInfo.Arguments = "-cp lib/AJPackage.jar ajinteractive.standard.utilities.updater.AjUpdaterApply \"" + args[0] + "\" \"" + args[1] + "\"";
					Process updateProcess = Process.Start(startInfo);
                    updateProcess.WaitForExit();
				} else {
					MessageBox.Show("Java doit être installé pour lancer l'application"); 
				}
			} else {
				Console.WriteLine("usage:");
				Console.WriteLine("concoursjeunes-applyupdate updatePath programPath");
				Console.WriteLine("");
				Console.WriteLine("\t* updatePath: the temp path who contains updated file");
				Console.WriteLine("\t* programPath: the definitive path of application file");
				Console.ReadLine();
				
				//Process.
			}
		}
	}
}

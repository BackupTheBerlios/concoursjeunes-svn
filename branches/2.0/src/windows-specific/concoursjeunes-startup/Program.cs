using System;
using System.Collections.Generic;
using System.Text;
using System.Diagnostics;
using Microsoft.Win32;
using System.Windows.Forms;

namespace concoursjeunes_startup {
	class Program {

		static void Main(string[] args) {
			string debug = "";

			if (args.Length > 0 && args[0] == "-debug") {
				debug = "-Ddebug.mode";
			}

			startApp(debug);
		}

		private static void startApp(string debug) {
			RegistryKey HKLM = Registry.LocalMachine;
			object version = HKLM.OpenSubKey(@"SOFTWARE\JavaSoft\Java Runtime Environment").GetValue(@"CurrentVersion");
			if (version != null) {
				object javaPath = HKLM.OpenSubKey(@"SOFTWARE\JavaSoft\Java Runtime Environment\" + (string)version).GetValue("JavaHome");

				ProcessStartInfo startInfo = new ProcessStartInfo(javaPath + @"\bin\javaw.exe");
				startInfo.Arguments = "-Xmx128m " + debug + " -jar ConcoursJeunes.jar";
				Process updateProcess = Process.Start(startInfo);
				updateProcess.WaitForExit();
				//si on sort avec un statut 3 alors redemarrer l'application
				if (updateProcess.ExitCode == 3) {
					startApp(debug);
				}
			} else {
				MessageBox.Show("Java doit �tre install� pour lancer l'application"); 
			}
		}
	}
}

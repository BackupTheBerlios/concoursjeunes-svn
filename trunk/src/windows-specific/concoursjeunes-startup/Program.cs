using System;
using System.Collections.Generic;
using System.Text;
using System.Diagnostics;
using Microsoft.Win32;
using System.Windows.Forms;
using System.Security.AccessControl;

namespace AJDeveloppement.ArcCompetition {
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
            RegistryKey javaReg = HKLM.OpenSubKey(@"SOFTWARE\JavaSoft\Java Runtime Environment");

            if(javaReg == null)
                javaReg = HKLM.OpenSubKey(@"SOFTWARE\Wow6432Node\JavaSoft\Java Runtime Environment");
        
			if (javaReg != null) {
                object version = javaReg.GetValue(@"CurrentVersion");
				object javaPath = javaReg.OpenSubKey((string)version).GetValue("JavaHome");

				ProcessStartInfo startInfo = new ProcessStartInfo(javaPath + @"\bin\javaw.exe");
				startInfo.Arguments = Properties.Settings.Default.VMArgs 
                    + " -Xmx" + Properties.Settings.Default.memoryMaxSize + " " 
                    + debug 
                    + " -jar " + Properties.Settings.Default.jarFile;
				Process updateProcess = Process.Start(startInfo);
				updateProcess.WaitForExit();
				//si on sort avec un statut 3 alors redemarrer l'application
				if (updateProcess.ExitCode == 3) {
					startApp(debug);
				}
			} else {
				MessageBox.Show("Java doit être installé pour lancer l'application"); 
			}
		}
	}
}

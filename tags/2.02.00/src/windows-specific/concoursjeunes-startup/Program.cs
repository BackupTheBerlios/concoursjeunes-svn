using System;
using System.Collections.Generic;
using System.Text;
using System.Diagnostics;

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
			ProcessStartInfo startInfo = new ProcessStartInfo(Environment.SystemDirectory + @"\javaw.exe");
			startInfo.Arguments = "-Xmx128m " + debug + " -jar ConcoursJeunes.jar";
			Process updateProcess = Process.Start(startInfo);
			updateProcess.WaitForExit();
			//si on sort avec un statut 3 alors redemarrer l'application
			if (updateProcess.ExitCode == 3) {
				startApp(debug);
			}
		}
	}
}

using System;
using System.Collections.Generic;
using System.Text;
using System.Diagnostics;
using System.IO;

namespace concoursjeunes_applyupdate {
	class Program {
		static void Main(string[] args) {
			if (args.Length == 2) {
				ProcessStartInfo startInfo = new ProcessStartInfo(Environment.SystemDirectory + @"\javaw.exe");
				startInfo.Arguments = "-cp lib/AJPackage.jar ajinteractive.standard.utilities.updater.AjUpdaterApply \"" + args[0] + "\" \"" + args[1] + "\"";
				Process updateProcess = Process.Start(startInfo);
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

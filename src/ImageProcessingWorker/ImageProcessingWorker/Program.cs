using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Diagnostics;
using System.Messaging;

namespace ImageProcessingWorker
{
    class Program
    {
        static void Main(string[] args)
        { Console.WriteLine("Worker is running");
            while (true)
            {
                MessageQueue messageQueue = null;

                using (messageQueue = new MessageQueue(@".\Private$\Images"))
                {
                    System.Messaging.Message[] messages = messageQueue.GetAllMessages();

                    foreach (System.Messaging.Message message in messages)
                    {
                        messageQueue.Formatter = new XmlMessageFormatter(new Type[] { typeof(Task) });
                        Task t = (Task)messageQueue.Receive().Body;
                        Console.WriteLine("New task "+t.name);
                        string preparedArguments = null;
                        if(t.height.Equals("") || t.width.Equals(""))
                        {
                            if (t.degrees.Equals("")) 
                            {
                                if (t.blackwhite.Equals("true")) //only b/w
                                {
                                    Console.WriteLine("//only b/w");
                                    preparedArguments = @"-colorSpace Gray D:\sisgui\sisgui\uploads\" + t.name + @" D:\sisgui\sisgui\results\" + t.name;
                                }
                                if (t.blackwhite.Equals("false"))//just copy
                                {

                                    Console.WriteLine("just copy");
                                    preparedArguments = @" D:\sisgui\sisgui\uploads\" + t.name + @" D:\sisgui\sisgui\results\" + t.name;
                                }
                            }
                            else
                            {
                                    if (t.blackwhite.Equals("true"))//rotate and b/w
                                    {

                                        Console.WriteLine("//rotate and b/w");
                                        preparedArguments = @"-rotate " + t.degrees + @" -colorSpace Gray D:\sisgui\sisgui\uploads\" + t.name + @" D:\sisgui\sisgui\results\" + t.name;
                                    }
                                    if (t.blackwhite.Equals("false"))//just rotate
                                    {

                                        Console.WriteLine("//just rotate");
                                        preparedArguments = @"-rotate " + t.degrees + @" D:\sisgui\sisgui\uploads\" + t.name + @" D:\sisgui\sisgui\results\" + t.name;
                                    }
                            }
                        }
                        else
                        {
                            if (t.degrees.Equals(""))
                            {
                                if (t.blackwhite.Equals("true")) //resize & b/w
                                {

                                    Console.WriteLine("resize and b/w");
                                    preparedArguments = @"-resize  " + t.height + "x" + t.width + @" -colorSpace Gray D:\sisgui\sisgui\uploads\" + t.name + @" D:\sisgui\sisgui\results\" + t.name;
                                }
                                if (t.blackwhite.Equals("false"))//just resize
                                {

                                    Console.WriteLine("//resize");
                                    preparedArguments = @"-resize  " + t.height + "x" + t.width + @" D:\sisgui\sisgui\uploads\" + t.name + @" D:\sisgui\sisgui\results\" + t.name;
                                }
                            }
                            else
                            {
                                if (t.blackwhite.Equals("true"))//resize, rotate and b/w
                                {

                                    Console.WriteLine("//resize, rotate and b/w");
                                    preparedArguments = @"-resize  " + t.height + "x" + t.width + @" -rotate " + t.degrees + @" -colorSpace Gray D:\sisgui\sisgui\uploads\" + t.name + @" D:\sisgui\sisgui\results\" + t.name;
                                }
                                if (t.blackwhite.Equals("false"))//resize & rotate
                                {
                                    Console.WriteLine("//resize & rotate");

                                    preparedArguments = @"-resize  " + t.height + "x" + t.width + @" -rotate " + t.degrees + @" D:\sisgui\sisgui\uploads\" + t.name + @" D:\sisgui\sisgui\results\" + t.name;
                                }

                            }

                        }
                        var proc = new Process
                        {
                            StartInfo = new ProcessStartInfo
                            {
                                FileName = @"C:\Program Files\ImageMagick-6.9.0-Q16\convert.exe",
                                Arguments = preparedArguments,
                                UseShellExecute = false,
                                RedirectStandardError = true,
                                CreateNoWindow = true
                            }
                        };

                        proc.Start();
                        string error = proc.StandardError.ReadToEnd();
                        proc.WaitForExit();
                        
                }
            }

        }
       }
    }
}

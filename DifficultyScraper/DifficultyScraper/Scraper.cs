using OpenQA.Selenium;
using OpenQA.Selenium.Chrome;
using OpenQA.Selenium.Support.UI;
using System;
using System.Collections.Generic;
using System.IO;
using System.Text.RegularExpressions;

namespace DifficultyScraper
{
    class Scraper
    {
        private ChromeDriver Driver;
        private Dictionary<int, List<string>> Puzzles;

        public Scraper()
        {
            Driver = new ChromeDriver();
            Puzzles = new Dictionary<int, List<string>>();
            for (int i = 1; i <= 6; i++)
                Puzzles.Add(i, new List<string>());
        }

        private void WriteLinesToEndOfFile(List<string> lines, string path)
        {
            if (!File.Exists(path))
            {
                var myFile = File.Create(path);
                myFile.Close();
            }

            using (StreamWriter sw = File.AppendText(path))
            {
                foreach (string line in lines)
                    sw.WriteLine(line);
            }
        }

        public void ScrapeDifficulties(string filepath)
        {
            Regex rx = new Regex("[\\d]");

            try
            {
                Driver.Manage().Timeouts().ImplicitWait = TimeSpan.FromSeconds(5);
                WebDriverWait wdw = new WebDriverWait(Driver, TimeSpan.FromSeconds(20));

                string line;
                List<string> puzzles = new List<string>();
                using (StreamReader sr = new StreamReader(filepath))
                {
                    while ((line = sr.ReadLine()) != null)
                    {
                        puzzles.Add(line);
                    }
                }


                foreach (string puzzle in puzzles)
                {
                    string url = "https://www.thonky.com/sudoku/evaluate-sudoku?puzzlebox=";
                    Driver.Navigate().GoToUrl(url + puzzle);

                    //var puzzleForm = Driver.FindElement(By.Name("puzzlebox"));
                    //puzzleForm.SendKeys(puzzle);

                    //var submit = wdw.Until(Driver => Driver.FindElement(By.XPath("/html/body/div/div[1]/div[2]/div[2]/div/article/span/form/button")));
                    //Driver.FindElement(By.XPath("/html/body/div/div[1]/div[2]/div[2]/div/article/span/form/button"));
                    //submit.Click();

                    string ratingString = wdw.Until(Driver => Driver.FindElement(By.XPath("/html/body/div/div[1]/div[2]/div[2]/div/article/span/div/p[2]/big")).Text);
                    int rating = int.Parse(rx.Matches(ratingString)[0].Value);
                    Puzzles[rating].Add(puzzle);
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }

            foreach (int rating in Puzzles.Keys)
            {
                Console.WriteLine(rating + ": " + Puzzles[rating].Count);
                WriteLinesToEndOfFile(Puzzles[rating], "C:\\Users\\drslc\\OneDrive\\Documents\\GitHub\\Sudoku\\src\\puzzles\\rated" + rating + ".txt");
            }
            Driver.Close();
            Driver.Quit();
        }


        static void Main(string[] args)
        {
            Scraper s = new Scraper();
            s.ScrapeDifficulties("C:\\Users\\drslc\\OneDrive\\Documents\\GitHub\\Sudoku\\src\\puzzles\\test8.txt");
            Environment.Exit(0);

        }
    }
}

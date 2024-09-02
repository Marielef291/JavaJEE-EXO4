package controller;

import Repository.ChienRepository;
import entity.Chien;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Cache;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import util.SessionFactorySingleton;


import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@WebServlet(value= {"/liste/*"})
public class ChienServlet extends HttpServlet {
    private ChienRepository chienRepository;


    public void init(){
        chienRepository = new ChienRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Chien> chiens = chienRepository.findAll();
        req.setAttribute("chiens",chiens);

        String pathInfo = (req.getPathInfo() != null && !req.getPathInfo().isEmpty()) ? req.getPathInfo() : "";

        // si j'ai autre chose que "" dans mon pathinfpo alors je vais aller recup la presonne correspondante
        if(Objects.equals(pathInfo, "/ChienDetail")){
//            System.out.println("Path info obtenue : "+pathInfo);
//            // methode .substring(1) pour enlever le premier caractere
//            System.out.println("Path info sans le / au debut : "+pathInfo.substring(1));
            Chien chien = chienRepository.findById(Integer.parseInt(req.getParameter("id")));
            req.setAttribute("chien",chien);
            req.getRequestDispatcher("/WEB-INF/ChienDetail.jsp").forward(req,resp);
        }



        req.getRequestDispatcher("/WEB-INF/Chien.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Chien chien = Chien.builder()
                .nom(req.getParameter("nom"))
                .race(req.getParameter("race"))
                .favRepas(req.getParameter("favRepas"))
                .dateOfBirth(LocalDate.parse(req.getParameter("dateOfBirth")))
                .build();

        chienRepository.save(chien);
        doGet(req,resp);
    }
}

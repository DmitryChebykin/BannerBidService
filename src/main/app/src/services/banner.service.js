import http from "../http-common";

class TutorialDataService {
    getAll() {
        return http.get("/banners/get_active");
    }

    get(id) {
        return http.get(`/banners/${id}`);
    }

    create(data) {
        return http.post("/banners/new", data);
    }

    update(id, data) {
        return http.patch(`/banners/patch/${id}`, data);
    }

    findByTitle(title) {
        return http.get("/banners", {params: {title}});
    }
}

export default new TutorialDataService();
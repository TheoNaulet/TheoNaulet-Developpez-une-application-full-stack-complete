export interface Article {
    id: number;
    title: string;
    content: string;
    createdAt: string;
    updatedAt?: string;
    authorId?: number;
    authorUsername?: string;
    themeId?: number;
    themeTitle?: string;
    comments?: Comment[];
    date?: string;
    author?: string;
}

export interface Comment {
    id: number;
    content: string;
    createdAt?: string;
    senderId?: number;
    senderUsername?: string;
    articleId?: number;
}

export interface ArticleFormData {
    themeId: number | undefined;
    title: string;
    content: string;
}

export interface ArticleDisplay {
    title: string;
    date: string;
    author: string;
    theme: string;
    content: string;
}

export interface CommentDisplay {
    username: string;
    content: string;
}

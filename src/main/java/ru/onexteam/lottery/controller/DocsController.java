package ru.onexteam.lottery.controller;

import io.javalin.Javalin;

public class DocsController {

    private static final String OPENAPI_JSON = """
            {
              "openapi": "3.0.3",
              "info": {
                "title": "API лотерейного сервиса",
                "version": "1.0.0",
                "description": "REST API для авторизации пользователей, управления тиражами, покупки билетов и получения результатов лотереи."
              },
              "servers": [
                {
                  "url": "http://localhost:8080",
                  "description": "Локальный сервер"
                }
              ],
              "tags": [
                {
                  "name": "Авторизация",
                  "description": "Регистрация и вход"
                },
                {
                  "name": "Тиражи",
                  "description": "Просмотр, создание и завершение тиражей"
                },
                {
                  "name": "Билеты",
                  "description": "Покупка билетов и результаты билетов пользователя"
                }
              ],
              "paths": {
                "/api/auth/register": {
                  "post": {
                    "tags": ["Авторизация"],
                    "summary": "Зарегистрировать пользователя",
                    "requestBody": {
                      "required": true,
                      "content": {
                        "application/json": {
                          "schema": {
                            "$ref": "#/components/schemas/AuthRequest"
                          },
                          "example": {
                            "email": "user@example.com",
                            "password": "password123"
                          }
                        }
                      }
                    },
                    "responses": {
                      "201": {
                        "description": "Пользователь зарегистрирован",
                        "content": {
                          "application/json": {
                            "schema": {
                              "$ref": "#/components/schemas/AuthResponse"
                            }
                          }
                        }
                      },
                      "400": {
                        "$ref": "#/components/responses/BadRequest"
                      },
                      "409": {
                        "$ref": "#/components/responses/Conflict"
                      }
                    }
                  }
                },
                "/api/auth/login": {
                  "post": {
                    "tags": ["Авторизация"],
                    "summary": "Войти",
                    "requestBody": {
                      "required": true,
                      "content": {
                        "application/json": {
                          "schema": {
                            "$ref": "#/components/schemas/AuthRequest"
                          },
                          "example": {
                            "email": "admin@lottery.local",
                            "password": "admin"
                          }
                        }
                      }
                    },
                    "responses": {
                      "200": {
                        "description": "Вход выполнен",
                        "content": {
                          "application/json": {
                            "schema": {
                              "$ref": "#/components/schemas/AuthResponse"
                            }
                          }
                        }
                      },
                      "400": {
                        "$ref": "#/components/responses/BadRequest"
                      },
                      "401": {
                        "$ref": "#/components/responses/Unauthorized"
                      }
                    }
                  }
                },
                "/api/user/draws": {
                  "get": {
                    "tags": ["Тиражи"],
                    "summary": "Получить активные тиражи",
                    "security": [
                      {
                        "bearerAuth": []
                      }
                    ],
                    "parameters": [
                      {
                        "name": "status",
                        "in": "query",
                        "required": false,
                        "schema": {
                          "type": "string",
                          "enum": ["active"]
                        },
                        "example": "active"
                      }
                    ],
                    "responses": {
                      "200": {
                        "description": "Активные тиражи",
                        "content": {
                          "application/json": {
                            "schema": {
                              "type": "array",
                              "items": {
                                "$ref": "#/components/schemas/Draw"
                              }
                            }
                          }
                        }
                      },
                      "400": {
                        "$ref": "#/components/responses/BadRequest"
                      },
                      "401": {
                        "$ref": "#/components/responses/Unauthorized"
                      }
                    }
                  }
                },
                "/api/user/draws/{drawId}": {
                  "get": {
                    "tags": ["Тиражи"],
                    "summary": "Получить тираж по id",
                    "security": [
                      {
                        "bearerAuth": []
                      }
                    ],
                    "parameters": [
                      {
                        "$ref": "#/components/parameters/DrawId"
                      }
                    ],
                    "responses": {
                      "200": {
                        "description": "Тираж",
                        "content": {
                          "application/json": {
                            "schema": {
                              "$ref": "#/components/schemas/Draw"
                            }
                          }
                        }
                      },
                      "400": {
                        "$ref": "#/components/responses/BadRequest"
                      },
                      "401": {
                        "$ref": "#/components/responses/Unauthorized"
                      },
                      "404": {
                        "$ref": "#/components/responses/NotFound"
                      }
                    }
                  }
                },
                "/api/user/draws/{drawId}/result": {
                  "get": {
                    "tags": ["Тиражи"],
                    "summary": "Получить результат тиража",
                    "security": [
                      {
                        "bearerAuth": []
                      }
                    ],
                    "parameters": [
                      {
                        "$ref": "#/components/parameters/DrawId"
                      }
                    ],
                    "responses": {
                      "200": {
                        "description": "Результат тиража",
                        "content": {
                          "application/json": {
                            "schema": {
                              "$ref": "#/components/schemas/DrawResult"
                            }
                          }
                        }
                      },
                      "400": {
                        "$ref": "#/components/responses/BadRequest"
                      },
                      "401": {
                        "$ref": "#/components/responses/Unauthorized"
                      },
                      "404": {
                        "$ref": "#/components/responses/NotFound"
                      }
                    }
                  }
                },
                "/api/admin/draws": {
                  "post": {
                    "tags": ["Тиражи"],
                    "summary": "Создать тираж",
                    "security": [
                      {
                        "bearerAuth": []
                      }
                    ],
                    "requestBody": {
                      "required": true,
                      "content": {
                        "application/json": {
                          "schema": {
                            "$ref": "#/components/schemas/CreateDrawRequest"
                          },
                          "example": {
                            "title": "Майская лотерея"
                          }
                        }
                      }
                    },
                    "responses": {
                      "201": {
                        "description": "Тираж создан",
                        "content": {
                          "application/json": {
                            "schema": {
                              "$ref": "#/components/schemas/Draw"
                            }
                          }
                        }
                      },
                      "400": {
                        "$ref": "#/components/responses/BadRequest"
                      },
                      "401": {
                        "$ref": "#/components/responses/Unauthorized"
                      },
                      "403": {
                        "$ref": "#/components/responses/Forbidden"
                      }
                    }
                  }
                },
                "/api/admin/draws/{drawId}/close": {
                  "post": {
                    "tags": ["Тиражи"],
                    "summary": "Закрыть тираж и провести лотерею",
                    "security": [
                      {
                        "bearerAuth": []
                      }
                    ],
                    "parameters": [
                      {
                        "$ref": "#/components/parameters/DrawId"
                      }
                    ],
                    "responses": {
                      "200": {
                        "description": "Тираж завершен",
                        "content": {
                          "application/json": {
                            "schema": {
                              "$ref": "#/components/schemas/DrawResult"
                            }
                          }
                        }
                      },
                      "400": {
                        "$ref": "#/components/responses/BadRequest"
                      },
                      "401": {
                        "$ref": "#/components/responses/Unauthorized"
                      },
                      "403": {
                        "$ref": "#/components/responses/Forbidden"
                      },
                      "404": {
                        "$ref": "#/components/responses/NotFound"
                      },
                      "409": {
                        "$ref": "#/components/responses/Conflict"
                      }
                    }
                  }
                },
                "/api/user/draws/{drawId}/tickets": {
                  "post": {
                    "tags": ["Билеты"],
                    "summary": "Купить билет на тираж",
                    "security": [
                      {
                        "bearerAuth": []
                      }
                    ],
                    "parameters": [
                      {
                        "$ref": "#/components/parameters/DrawId"
                      }
                    ],
                    "responses": {
                      "201": {
                        "description": "Билет создан",
                        "content": {
                          "application/json": {
                            "schema": {
                              "$ref": "#/components/schemas/Ticket"
                            }
                          }
                        }
                      },
                      "400": {
                        "$ref": "#/components/responses/BadRequest"
                      },
                      "401": {
                        "$ref": "#/components/responses/Unauthorized"
                      },
                      "404": {
                        "$ref": "#/components/responses/NotFound"
                      },
                      "409": {
                        "$ref": "#/components/responses/Conflict"
                      }
                    }
                  }
                },
                "/api/user/me/tickets": {
                  "get": {
                    "tags": ["Билеты"],
                    "summary": "Получить билеты текущего пользователя",
                    "security": [
                      {
                        "bearerAuth": []
                      }
                    ],
                    "responses": {
                      "200": {
                        "description": "Билеты пользователя",
                        "content": {
                          "application/json": {
                            "schema": {
                              "type": "array",
                              "items": {
                                "$ref": "#/components/schemas/Ticket"
                              }
                            }
                          }
                        }
                      },
                      "401": {
                        "$ref": "#/components/responses/Unauthorized"
                      }
                    }
                  }
                },
                "/api/user/me/tickets/{ticketId}": {
                  "get": {
                    "tags": ["Билеты"],
                    "summary": "Получить билет текущего пользователя по id",
                    "security": [
                      {
                        "bearerAuth": []
                      }
                    ],
                    "parameters": [
                      {
                        "$ref": "#/components/parameters/TicketId"
                      }
                    ],
                    "responses": {
                      "200": {
                        "description": "Билет",
                        "content": {
                          "application/json": {
                            "schema": {
                              "$ref": "#/components/schemas/Ticket"
                            }
                          }
                        }
                      },
                      "400": {
                        "$ref": "#/components/responses/BadRequest"
                      },
                      "401": {
                        "$ref": "#/components/responses/Unauthorized"
                      },
                      "404": {
                        "$ref": "#/components/responses/NotFound"
                      }
                    }
                  }
                },
                "/api/user/me/tickets/{ticketId}/result": {
                  "get": {
                    "tags": ["Билеты"],
                    "summary": "Получить результат билета текущего пользователя",
                    "security": [
                      {
                        "bearerAuth": []
                      }
                    ],
                    "parameters": [
                      {
                        "$ref": "#/components/parameters/TicketId"
                      }
                    ],
                    "responses": {
                      "200": {
                        "description": "Результат билета",
                        "content": {
                          "application/json": {
                            "schema": {
                              "$ref": "#/components/schemas/Ticket"
                            }
                          }
                        }
                      },
                      "400": {
                        "$ref": "#/components/responses/BadRequest"
                      },
                      "401": {
                        "$ref": "#/components/responses/Unauthorized"
                      },
                      "404": {
                        "$ref": "#/components/responses/NotFound"
                      },
                      "409": {
                        "$ref": "#/components/responses/Conflict"
                      }
                    }
                  }
                }
              },
              "components": {
                "securitySchemes": {
                  "bearerAuth": {
                    "type": "http",
                    "scheme": "bearer",
                    "bearerFormat": "JWT"
                  }
                },
                "parameters": {
                  "DrawId": {
                    "name": "drawId",
                    "in": "path",
                    "required": true,
                    "schema": {
                      "type": "integer",
                      "format": "int64"
                    },
                    "example": 1
                  },
                  "TicketId": {
                    "name": "ticketId",
                    "in": "path",
                    "required": true,
                    "schema": {
                      "type": "integer",
                      "format": "int64"
                    },
                    "example": 1
                  }
                },
                "schemas": {
                  "AuthRequest": {
                    "type": "object",
                    "required": ["email", "password"],
                    "properties": {
                      "email": {
                        "type": "string",
                        "format": "email",
                        "example": "user@example.com"
                      },
                      "password": {
                        "type": "string",
                        "format": "password",
                        "example": "password123"
                      }
                    }
                  },
                  "AuthResponse": {
                    "type": "object",
                    "properties": {
                      "token": {
                        "type": "string",
                        "example": "eyJhbGciOiJIUzI1NiJ9..."
                      },
                      "role": {
                        "type": "string",
                        "enum": ["USER", "ADMIN"],
                        "example": "USER"
                      }
                    }
                  },
                  "CreateDrawRequest": {
                    "type": "object",
                    "required": ["title"],
                    "properties": {
                      "title": {
                        "type": "string",
                        "example": "Майская лотерея"
                      }
                    }
                  },
                  "Draw": {
                    "type": "object",
                    "properties": {
                      "id": {
                        "type": "integer",
                        "format": "int64",
                        "example": 1
                      },
                      "title": {
                        "type": "string",
                        "example": "Майская лотерея"
                      },
                      "status": {
                        "type": "string",
                        "enum": ["ACTIVE", "FINISHED"],
                        "example": "ACTIVE"
                      }
                    }
                  },
                  "DrawResult": {
                    "type": "object",
                    "properties": {
                      "id": {
                        "type": "integer",
                        "format": "int64",
                        "example": 1
                      },
                      "drawId": {
                        "type": "integer",
                        "format": "int64",
                        "example": 1
                      },
                      "winningCombination": {
                        "type": "string",
                        "example": "3,11,19,22,36,44"
                      }
                    }
                  },
                  "Ticket": {
                    "type": "object",
                    "properties": {
                      "id": {
                        "type": "integer",
                        "format": "int64",
                        "example": 1
                      },
                      "userId": {
                        "type": "integer",
                        "format": "int64",
                        "example": 1
                      },
                      "drawId": {
                        "type": "integer",
                        "format": "int64",
                        "example": 1
                      },
                      "combination": {
                        "type": "string",
                        "example": "1,7,15,28,31,49"
                      },
                      "status": {
                        "type": "string",
                        "enum": ["PENDING", "WIN", "LOSE"],
                        "example": "PENDING"
                      }
                    }
                  },
                  "Error": {
                    "type": "string",
                    "example": "Текст ошибки"
                  }
                },
                "responses": {
                  "BadRequest": {
                    "description": "Некорректный запрос",
                    "content": {
                      "text/plain": {
                        "schema": {
                          "$ref": "#/components/schemas/Error"
                        }
                      }
                    }
                  },
                  "Unauthorized": {
                    "description": "Не авторизован",
                    "content": {
                      "text/plain": {
                        "schema": {
                          "$ref": "#/components/schemas/Error"
                        }
                      }
                    }
                  },
                  "Forbidden": {
                    "description": "Доступ запрещен",
                    "content": {
                      "text/plain": {
                        "schema": {
                          "$ref": "#/components/schemas/Error"
                        }
                      }
                    }
                  },
                  "NotFound": {
                    "description": "Не найдено",
                    "content": {
                      "text/plain": {
                        "schema": {
                          "$ref": "#/components/schemas/Error"
                        }
                      }
                    }
                  },
                  "Conflict": {
                    "description": "Конфликт состояния",
                    "content": {
                      "text/plain": {
                        "schema": {
                          "$ref": "#/components/schemas/Error"
                        }
                      }
                    }
                  }
                }
              }
            }
            """;

    private static final String DOCS_HTML = """
            <!doctype html>
            <html lang="ru">
            <head>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <title>Документация API лотерейного сервиса</title>
                <link rel="stylesheet" href="https://unpkg.com/swagger-ui-dist@5.17.14/swagger-ui.css">
                <style>
                    body { margin: 0; background: #f6f8fa; }
                    .topbar { display: none; }
                </style>
            </head>
            <body>
                <div id="swagger-ui"></div>
                <script src="https://unpkg.com/swagger-ui-dist@5.17.14/swagger-ui-bundle.js"></script>
                <script>
                    window.ui = SwaggerUIBundle({
                        url: '/openapi.json',
                        dom_id: '#swagger-ui',
                        deepLinking: true,
                        persistAuthorization: true,
                        displayRequestDuration: true,
                        tryItOutEnabled: true
                    });
                </script>
            </body>
            </html>
            """;

    public static void register(Javalin app) {
        app.get("/openapi.json", ctx -> ctx.contentType("application/json").result(OPENAPI_JSON));
        app.get("/docs", ctx -> ctx.contentType("text/html").result(DOCS_HTML));
    }
}
